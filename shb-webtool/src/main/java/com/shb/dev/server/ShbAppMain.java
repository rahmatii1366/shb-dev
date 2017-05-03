package com.shb.dev.server;

import com.shb.dev.server.http.ShbBaseHttpServer;

import java.io.InputStream;

/**
 * @author Mohammad Rahmati, 4/23/2017 12:13 PM
 */
public class ShbAppMain implements Runnable {
    private InputStream serverConfigStream = null;
    private InputStream routerConfigStream = null;

    public ShbAppMain(
            InputStream serverConfigStream,
            InputStream routerConfigStream) {
        this.serverConfigStream = serverConfigStream;
        this.routerConfigStream = routerConfigStream;
    }

    @Override
    public void run() {
        try {
            ShbBaseHttpServer server =
                    ShbBaseHttpServer.createServer(
                            serverConfigStream,
                            routerConfigStream);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
