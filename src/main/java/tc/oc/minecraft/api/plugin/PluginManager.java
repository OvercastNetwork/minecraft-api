package tc.oc.minecraft.api.plugin;

import java.util.Collection;

import tc.oc.minecraft.api.event.EventBus;

public interface PluginManager extends EventBus {

    Plugin getPlugin(String name);

    Collection<? extends Plugin> getAllPlugins();
}
