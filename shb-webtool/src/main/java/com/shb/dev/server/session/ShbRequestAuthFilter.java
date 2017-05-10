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
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Mohammad Rahmati, 4/12/2017 2:33 PM
 */
@ShbRole(roleType = ShbRoleType.ADMIN)
@Singleton
@Deprecated
public class ShbRequestAuthFilter extends ShbAuthFilter
        implements ContainerRequestFilter {
    final static Logger logger =
            Logger.getLogger(ShbRequestAuthFilter.class);
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
        SessionName = sessionConfig.getSessionName();
        sessionManager = (ShbSessionManager) config
                .getProperty(ShbSessionManager
                        .SHB_SESSION_MANAGER);
    }

    public void filter(
            ContainerRequestContext requestContext)
            throws IOException {

        Cookie cookie = requestContext.getCookies()
                .get(SessionName);

        ShbRoleType sessionRoleType = ShbRoleType.GUEST;
//        if(cookie != null) {
//            ShbSession shbSession = sessionManager
//                    .retrieveSession(requestContext.getHeaders());
//            if(shbSession != null)
//                sessionRoleType = shbSession.getRoleType();
//        }

        ShbRoleType resourceRuleType =
                getResourceRuleType(requestContext);

        if(!resourceRuleType.isValid(sessionRoleType)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .type("text/plain; charset=UTF-8")
                            .build());
        }
    }
}
