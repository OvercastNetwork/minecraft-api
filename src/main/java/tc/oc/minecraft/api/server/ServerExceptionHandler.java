package tc.oc.minecraft.api.server;

import javax.inject.Inject;

import tc.oc.exception.LoggingExceptionHandler;

public class ServerExceptionHandler extends LoggingExceptionHandler {

    @Inject ServerExceptionHandler(LocalServer localServer) {
        super(localServer.getLogger());
    }
}
