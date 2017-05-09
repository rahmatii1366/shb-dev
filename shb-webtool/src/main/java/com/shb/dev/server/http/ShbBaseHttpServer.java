package com.shb.dev.server.http;

import com.shb.dev.server.config.ShbConfigReader;
import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.config.ShbServerConfig.ShbSessionConfig;
import com.shb.dev.server.filter.response.ShbCORSFilter;
import com.shb.dev.server.route.ShbRouteClassGenerator;
import com.shb.dev.server.route.ShbRouteService;
import com.shb.dev.server.session.ShbRequestAuthFilter;
import com.shb.dev.server.session.ShbResponseAuthFilter;
import com.shb.dev.server.session.ShbSessionManager;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Rahmati, 4/23/2017 11:36 AM
 */
public abstract class ShbBaseHttpServer {
    protected Map<String, Object> httpProperties
            = new LinkedHashMap<>();
    protected ResourceConfig resourceConfig
            = new ResourceConfig();
    protected ShbServerConfig serverConfig = null;
    protected ShbSessionConfig sessionConfig = null;
    protected ShbRouterConfig routerConfig = null;
    protected ShbSessionManager sessionManager = null;

    public static ShbBaseHttpServer createServer(
            InputStream serverConfigStream,
            InputStream routerConfigStream)
            throws Exception {
        ShbServerConfig shbServerConfig =
                new ShbServerConfig();
        shbServerConfig.reconfigure(ShbConfigReader
                .createFromJson(serverConfigStream));
        ShbRouterConfig shbRouterConfig =
                new ShbRouterConfig();
        shbRouterConfig.reconfigure(ShbConfigReader
                .createFromJson(routerConfigStream));
        ShbHttpServerType serverType =
                shbServerConfig.getServerType();

        if(ShbHttpServerType.NETTY == serverType)
            return new ShbNettyHttpServer(
                    shbServerConfig, shbRouterConfig);
        else
            throw new Exception("type of http server not founded.");
    }

    public void addProperties(
            String key, Object property) {
        httpProperties.put(key, property);
    }

    public void start()
            throws Exception {
//        ArrayList<String> packages = (ArrayList<String>) serverConfig
//                .getValue(ShbServerConfig.REST_PACKAGES);
//        String[] packArray = new String[packages.size()];
//        int temp = 0;
//        for(String pack : packages)
//            packArray[temp++] = pack;
//        resourceConfig.packages(packArray);
        Set<Class<?>> classes = ShbRouteClassGenerator
                    .generateRouteClasses(routerConfig);
        resourceConfig.registerClasses(classes);
//        resourceConfig.register(ShbRouteService.class);
        resourceConfig.register(ShbRequestAuthFilter.class);
        resourceConfig.register(ShbResponseAuthFilter.class);
        resourceConfig.register(ShbCORSFilter.class);

        sessionManager = ShbSessionManager
                .createSessionManager(sessionConfig);
//        ArrayList<String> filters = (ArrayList<String>) serverConfig
//                .getValue(ShbServerConfig.REST_FILTERS);
//        for(String filter : filters)
//            try {
//                resourceConfig.register(Class.forName(filter));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        httpProperties.put(
                ShbServerConfig.SHB_SERVER_CONFIG,
                serverConfig);
        httpProperties.put(
                ShbSessionManager.SHB_SESSION_MANAGER,
                sessionManager);
        resourceConfig.addProperties(httpProperties);
        startService();
    }

    protected abstract void startService();
}
