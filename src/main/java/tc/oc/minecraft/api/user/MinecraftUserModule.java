package tc.oc.minecraft.api.user;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;

public class MinecraftUserModule extends AbstractModule {

    @Override
    protected void configure() {
        UserSourceBinder sources = new UserSourceBinder(binder());
        sources.addBinding().to(MemoryUserCache.class).asEagerSingleton();
        sources.addBinding().to(SQLUserCache.class).asEagerSingleton();

        OptionalBinder.newOptionalBinder(binder(), UserFactory.class)
                      .setDefault().to(SimpleUser.Factory.class);

        bind(UserFinder.class)
            .to(UserFinderImpl.class)
            .asEagerSingleton();
    }
}
