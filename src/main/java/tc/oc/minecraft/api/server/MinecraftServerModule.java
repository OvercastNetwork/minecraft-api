package tc.oc.minecraft.api.server;

import com.google.inject.AbstractModule;
import tc.oc.minecraft.api.scheduler.MinecraftSchedulerModule;
import tc.oc.minecraft.api.user.MinecraftUserModule;

public class MinecraftServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new MinecraftSchedulerModule());
        install(new MinecraftUserModule());

        bind(ServerExceptionHandler.class);
    }
}
