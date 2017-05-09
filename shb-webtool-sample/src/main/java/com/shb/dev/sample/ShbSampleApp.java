package com.shb.dev.sample;

import com.shb.dev.server.ShbAppMain;

import java.io.InputStream;

/**
 * @author Mohammad Rahmati, 5/3/2017 7:29 PM
 */
public class ShbSampleApp {
    public static void main(String[] args)
            throws Exception {
        InputStream serverConfigStream = ShbSampleApp
                .class.getResourceAsStream(
                        "/shb-server-config.json");
        InputStream routeConfigStream = ShbSampleApp
                .class.getResourceAsStream(
                        "/shb-route-config.json");
        ShbAppMain.run(
                serverConfigStream, routeConfigStream);
    }
}
