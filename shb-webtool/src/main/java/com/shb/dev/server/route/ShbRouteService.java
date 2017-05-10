package com.shb.dev.server.route;

import com.shb.dev.server.asset.ShbAsset;
import com.shb.dev.server.asset.ShbAssetResolver;
import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.response.ShbResponse;
import com.shb.dev.server.role.ShbRole;
import com.shb.dev.server.session.ShbSession;
import com.shb.dev.server.session.ShbSessionManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Rahmati, 5/3/2017 7:28 AM
 */
public class ShbRouteService {
    protected Logger logger = Logger
            .getLogger(ShbRouteService.class);
    @Context
    protected Configuration config;
    protected ShbServerConfig shbServerConfig = null;
    protected ShbSessionManager sessionManager = null;
    protected Map<String, Method> methodMap =
            new LinkedHashMap<>();
    protected static ShbResponse UNAUTHORIZED_RESPONSE =
            new ShbResponse(Status.UNAUTHORIZED, null);
    protected static ShbResponse INTERNAL_SERVER_ERROR_RESPONSE =
            new ShbResponse(Status.INTERNAL_SERVER_ERROR, null);

    public ShbRouteService() {
    }

    @PostConstruct
    protected void init()
            throws Exception {
        shbServerConfig = (ShbServerConfig) config
                .getProperty(ShbServerConfig
                        .SHB_SERVER_CONFIG);
//        File file = new File((String) shbServerConfig.getAssetPath());
//        assetResolver = ShbAssetResolver.getInstance(file.getPath());
        sessionManager = (ShbSessionManager) config
                .getProperty(ShbSessionManager
                        .SHB_SESSION_MANAGER);
    }

    protected Method registerMethod(
            String path,
            String callClassName,
            String callMethodName,
            Class<?>... paramList
    ) throws Exception {
        Method m = methodMap.get(path);
        if(m == null) {
            Class c = Class.forName(callClassName);
            m = c.getDeclaredMethod(
                    callMethodName, paramList);
            methodMap.put(path, m);
            return m;
        } else
            return m;
    }

    protected ShbSession doAuthorization(
            HttpHeaders httpHeaders) {
        return sessionManager
                .retrieveSession(httpHeaders);
    }

    protected ShbResponse invokeMethod(
            Method method,
            Object... parameters
    ) throws Exception {
        return (ShbResponse) method
                .invoke(null, parameters);
    }

    protected Response createResponse(
            ShbResponse response, ShbSession session) {
        NewCookie sessionCookie = sessionManager
                .registerCookie(session);
        Response.ResponseBuilder resBuilder = Response.status(
                response.getResponseStatus())
                .entity(response.getEntity());
        if(sessionCookie != null)
            resBuilder.cookie(sessionCookie);
        return resBuilder.build();
    }
}
