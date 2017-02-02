package tc.oc.minecraft.api.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Throwables;

@Singleton
public class SQLUserCache implements UserSource {

    private final UserFactory factory;
    private final Connection db;
    private final Object lock = new Object();

    @Inject SQLUserCache(UserFactory factory) {
        this.factory = factory;

        try {
            Class.forName("org.sqlite.JDBC");
            // We may use this database for other things in the future,
            // hence the name "server.db"
            this.db = DriverManager.getConnection("jdbc:sqlite:server.db");
        } catch(Exception e) {
            throw Throwables.propagate(e);
        }
        init();
    }

    private void init() {
        try(Statement statement = db.createStatement()) {
            statement.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "uuid CHAR(36) PRIMARY KEY NOT NULL, " +
                "name CHAR(16) NOT NULL, " +
                "name_lower CHAR(16) NOT NULL, " +
                "updated_at CHAR(20) NOT NULL);"
            );
        } catch(SQLException e) {
            throw Throwables.propagate(e);
        }
    }

    private User create(ResultSet result) throws SQLException {
        return factory.createUser(
            UUID.fromString(result.getString("uuid")),
            result.getString("name"),
            Instant.parse(result.getString("updated_at"))
        );
    }

    @Override
    public void update(User user) {
        if(user.hasValidId()) {
            synchronized(lock) {
                try(PreparedStatement statement = db.prepareStatement("INSERT OR REPLACE INTO users (uuid, name, name_lower, updated_at) VALUES (?,?,?,?);")) {
                    statement.setString(1, user.getUniqueId().toString());
                    statement.setString(2, user.name().get());
                    statement.setString(3, user.name().get().toLowerCase(Locale.ROOT));
                    statement.setString(4, user.updatedAt().get().toString());
                    statement.execute();
                } catch(SQLException e) {
                    throw Throwables.propagate(e);
                }
            }
        }
    }

    private @Nullable User load(String field, String value) throws SQLException {
        try(final PreparedStatement statement = db.prepareStatement("SELECT * FROM users WHERE " + field + "=?")) {
            statement.setString(1, value);
            try(final ResultSet result = statement.executeQuery()) {
                if(result.next()) {
                    return create(result);
                }
            }
        }
        return null;
    }

    @Override
    public User search(UserSearch search, Supplier<User> next) {
        final String field = search.query() instanceof UUID ? "uuid" : "name_lower";
        final String value = search.query().toString();
        try {
            // First, query without locking
            User user = load(field, value);
            if(user != null && search.filter().test(user)) return user;

            if(search.sync()) {
                // If query fails and we are on the main thread, just skip to the next source
                return next.get();
            } else {
                synchronized(lock) {
                    // If we're not on the main thread, lock and repeat the query
                    user = load(field, value);
                    if(user != null && search.filter().test(user)) return user;

                    // If the locked query also fails, get the next source and save it if it's complete
                    user = next.get();
                    update(user);
                    return user;
                }
            }
        } catch(SQLException e) {
            throw Throwables.propagate(e);
        }
    }
}
