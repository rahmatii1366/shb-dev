package com.shb.dev.server.session;

import com.shb.dev.server.config.ShbServerConfig.ShbSessionConfig;
import com.shb.dev.server.role.ShbRole;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.role.ShbRoleType;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.internal.routing.UriRoutingContext;
import org.glassfish.jersey.server.model.ResourceMethodInvoker;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Mohammad Rahmati, 4/23/2017 11:27 AM
 */
@ShbRole
@Singleton
@Deprecated
public class ShbResponseAuthFilter
        extends ShbAuthFilter
        implements ContainerResponseFilter {
    final static Logger logger =
            Logger.getLogger(ShbResponseAuthFilter.class);
    @Context
    private Configuration config;
    private ShbServerConfig serverConfig = null;
    private ShbSessionConfig sessionConfig = null;
    private ShbSessionManager sessionManager = null;
    private String SessionName;

    @PostConstruct
    public void init() {
        serverConfig = (ShbServerConfig) config
                .getProperty(ShbServerConfig
                        .SHB_SERVER_CONFIG);
        sessionConfig = serverConfig.getSessionConfig();
        SessionName = (String) sessionConfig.getSessionName();
        sessionManager = (ShbSessionManager) config
                .getProperty(ShbSessionManager
                        .SHB_SESSION_MANAGER);
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
            throws IOException {

        Cookie cookie = requestContext.getCookies()
                .get(SessionName);

//        sessionManager.setSessionCookie(
//                responseContext.getHeaders(),
//                cookie,
//                SessionName,
//                (int)sessionConfig.getSessionExpireSecond());
    }
}
