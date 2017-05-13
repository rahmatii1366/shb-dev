package com.shb.dev.server.config;

import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Rahmati, 4/23/2017 11:41 AM
 */
public class ShbRouterConfig
        extends ShbConfig {
    public static final String SHB_ROUTER_CONFIG =
            "shb-router-config";
    public static final String METHOD_CONFIG
            = "method";

    private static final Logger logger =
            Logger.getLogger(
                    ShbRouterConfig.class);

    public Set<String> getRoutes() {
        return configMap.keySet();
    }

    public Set<String> getHttpMethods(
            String route) {
        if (route == null || route.isEmpty())
            route = new String("/");
        if (getShbConfig(route) == null)
            return null;
        Map<String, Object> configMap = getMap(route);
        if (configMap != null)
            return configMap.keySet();
        return null;
    }

    public ShbRouteConfig getRouteConfig(
            String route, String method) {
        if (route == null || route.isEmpty())
            route = new String("/");
        if (getShbConfig(route) == null)
            return null;
        Map<String, Object> configMap = getMap(route);
        if (configMap.containsKey(method)) {
            ShbRouteConfig routeConfig =
                    new ShbRouteConfig();
            routeConfig.reconfigure((Map<String, Object>)
                    configMap.get(method));
            return routeConfig;
        }
        return null;
    }

//    public String get() {
//        return getValue();
//    }

    public static class ShbRouteConfig
            extends ShbConfig {
        public static final String HANDLER_CONFIG
                = "handler";
        public static final String PATH_PARAMS_CONFIG
                = "path-params";
        public static final String QUERY_PARAMS_CONFIG
                = "query-params";
        public static final String ROLE_CONFIG
                = "role";
        public static final String ASSET_PATH_CONFIG
                = "asset-path";

        public String getHandler() {
            return getString(HANDLER_CONFIG);
        }

        public List<String> getPathParams() {
            return getList(PATH_PARAMS_CONFIG);
        }

        public List<String> getQueryParams() {
            return getList(QUERY_PARAMS_CONFIG);
        }

        public String getRole() {
            return getString(ROLE_CONFIG);
        }

        public String getAssetPath() {
            return getString(ASSET_PATH_CONFIG);
        }

        public boolean isAsset() {
            if (getString(ASSET_PATH_CONFIG) != null) {
                File file = new File(
                        getString(ASSET_PATH_CONFIG));
                if (file.exists() && file.isDirectory())
                    return true;
            }
            return false;
        }
    }
}
