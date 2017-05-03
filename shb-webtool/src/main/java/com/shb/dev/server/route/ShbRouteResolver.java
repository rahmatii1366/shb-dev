package com.shb.dev.server.route;

import com.shb.dev.server.asset.ShbAsset;
import com.shb.dev.server.asset.ShbAssetResolver;
import com.shb.dev.server.config.ShbRouterConfig;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriInfo;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mohammad Rahmati, 5/3/2017 10:57 AM
 */
public class ShbRouteResolver {
    private Logger logger = Logger.getLogger(
            ShbRouteResolver.class);
    private Map<String, ShbRoute> routeMap =
            new LinkedHashMap<>();
    private static ReentrantLock lock = new ReentrantLock();
    protected ShbRouterConfig routerConfig;

    ShbRouteResolver(ShbRouterConfig routerConfig) {
        this.routerConfig = routerConfig;
        Set<String> routes = routerConfig.getRoutes();
        for(String route : routes) {
            int colon = route.indexOf(":");
            String url = route.substring(0,
                    route.indexOf(":") - 1);

        }
    }

    public void resolveRoute(
            String route,
            String requestMethod,
            UriInfo info) {
        ShbRouterConfig.ShbRouteConfig routeConfig =
                routerConfig.getRouteConfig(route, requestMethod);
    }
}
