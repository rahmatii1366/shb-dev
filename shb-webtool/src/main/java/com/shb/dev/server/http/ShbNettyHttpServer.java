package com.shb.dev.server.http;

import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.config.ShbServerConfig.ShbSessionConfig;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;

/**
 * @author Mohammad Rahmati, 4/12/2017 2:21 PM
 */
class ShbNettyHttpServer
        extends ShbBaseHttpServer {
    final static Logger logger =
            Logger.getLogger(ShbNettyHttpServer.class);
    private Channel channel = null;

    ShbNettyHttpServer(
            ShbServerConfig serverConfig,
            ShbRouterConfig routerConfig) {
        this.serverConfig = serverConfig;
        this.routerConfig = routerConfig;
        this.sessionConfig = new ShbSessionConfig();
        this.sessionConfig.reconfigure(
                serverConfig.getSessionConfig());
    }

    @Override
    protected void startService() {
        logger.info("initializing http server....");
        channel = NettyHttpContainerProvider
                .createServer(serverConfig.getBaseUri(),
                        resourceConfig, false);
        logger.info("http server started....");
    }
}
