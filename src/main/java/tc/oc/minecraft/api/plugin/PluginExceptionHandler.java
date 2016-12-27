package tc.oc.minecraft.api.plugin;

import javax.inject.Inject;

import tc.oc.exception.LoggingExceptionHandler;

public class PluginExceptionHandler extends LoggingExceptionHandler {
    @Inject public PluginExceptionHandler(Plugin plugin) {
        super(plugin.getLogger());
    }
}
