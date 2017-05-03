package com.shb.dev.server.filter.response;

import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.config.ShbServerConfig.ShbCORSConfig;
import com.shb.dev.server.session.ShbSessionManager;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * @author Mohammad Rahmati, 4/12/2017 2:27 PM
 */
@Singleton
public class ShbCORSFilter
        implements ContainerResponseFilter {
    @Context
    private Configuration config;
    private ShbServerConfig serverConfig = null;
    private ShbCORSConfig corsConfig = null;

    @PostConstruct
    public void init() {
        serverConfig = (ShbServerConfig) config
                .getProperty(ShbServerConfig
                        .SHB_SERVER_CONFIG);
        corsConfig = serverConfig.getCORSConfig();
    }

    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response)
            throws IOException {
        response.getHeaders().addAll(
                "Access-Control-Allow-Origin",
                corsConfig.getAccessControlAllowOrigin());
        response.getHeaders().addAll(
                "Access-Control-Allow-Headers",
                corsConfig.getAccessControlAllowHeaders());
        response.getHeaders().add(
                "Access-Control-Allow-Credentials",
                corsConfig.getAccessControlAllowCredentials());
        response.getHeaders().addAll(
                "Access-Control-Allow-Methods",
                corsConfig.getAccessControlAllowMethods());
    }
}
