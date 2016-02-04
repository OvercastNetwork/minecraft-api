package tc.oc.minecraft.api.event;

import tc.oc.minecraft.api.plugin.Plugin;

public interface EventBus {

    void registerListener(Plugin plugin, Listener listener);

    void unregisterListener(Listener listener);

    void unregisterListeners(Plugin plugin);
}
