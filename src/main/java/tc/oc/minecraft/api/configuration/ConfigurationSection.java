package tc.oc.minecraft.api.configuration;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

public interface ConfigurationSection {

    String getCurrentPath();

    String resolvePath(String key);

    ConfigurationSection getSection(String path);

    Collection<String> getKeys();

    @Nullable <T> T getType(String path, Class<T> type);

    @Nullable <T> T get(String path, T def);

    @Nullable Object get(String path);

    void set(String path, Object value);

    int getInt(String path);

    int getInt(String path, int def);

    long getLong(String path);

    long getLong(String path, long def);

    double getDouble(String path);

    double getDouble(String path, double def);

    boolean getBoolean(String path);

    boolean getBoolean(String path, boolean def);

    @Nullable String getString(String path);

    String getString(String path, String def);

    @Nullable <T> T getParsed(String path, Function<? super String, ? extends T> parser) throws InvalidConfigurationException;

    <T> T getParsed(String path, T def, Function<? super String, ? extends T> parser) throws InvalidConfigurationException;

    @Nullable Duration getDuration(String path) throws InvalidConfigurationException;

    default Optional<Duration> duration(String path) throws InvalidConfigurationException {
        return Optional.ofNullable(getDuration(path));
    }

    Duration getDuration(String path, Duration def) throws InvalidConfigurationException;

    <T> List<T> getListOf(String path, Class<T> type);

    List<?> getList(String path);

    List<?> getList(String path, List<?> def);

    List<String> getStringList(String path);

    List<Byte> getByteList(String path);

    List<Short> getShortList(String path);

    List<Long> getLongList(String path);

    List<Float> getFloatList(String path);

    List<Double> getDoubleList(String path);

    List<Boolean> getBooleanList(String path);

    <T> T needType(String path, Class<T> type) throws InvalidConfigurationException;

    Object need(String path) throws InvalidConfigurationException;

    ConfigurationSection needSection(String path) throws InvalidConfigurationException;

    int needInt(String path) throws InvalidConfigurationException;

    long needLong(String path) throws InvalidConfigurationException;

    double needDouble(String path) throws InvalidConfigurationException;

    boolean needBoolean(String path) throws InvalidConfigurationException;

    String needString(String path) throws InvalidConfigurationException;

    <T> T needParsed(String path, Function<? super String, ? extends T> parser) throws InvalidConfigurationException;

    Duration needDuration(String path) throws InvalidConfigurationException;

    List<?> needList(String path) throws InvalidConfigurationException;

    <T> List<T> needList(String path, Class<T> type) throws InvalidConfigurationException;
}
