package com.shb.dev.server;

import com.shb.dev.server.http.ShbBaseHttpServer;

import java.io.InputStream;

/**
 * @author Mohammad Rahmati, 4/23/2017 12:13 PM
 */
public class ShbAppMain {
    public static void run(
            InputStream serverConfigStream,
            InputStream routerConfigStream)
            throws Exception {
        ShbBaseHttpServer server =
                ShbBaseHttpServer.createServer(
                        serverConfigStream,
                        routerConfigStream);
        server.start();
    }
}
