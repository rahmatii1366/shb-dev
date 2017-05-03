package com.shb.dev.server.config;

import com.shb.dev.server.http.ShbHttpServerType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

/**
 * @author Mohammad Rahmati, 4/23/2017 11:41 AM
 */
public class ShbServerConfig
        extends ShbConfig {
    public static final String SHB_SERVER_CONFIG =
            "shb-server-config";

    private static final Logger logger =
            Logger.getLogger(
                    ShbServerConfig.class);
    public static final String SERVER_TYPE
            = "server-type";
    public static final String HTTP_IP
            = "http-ip";
    public static final String HTTP_BASE_ROUTE
            = "http-base-route";
    public static final String HTTP_PORT
            = "http-port";
    public static final String SESSION_CONFIG
            = "session";
    public static final String CORS_CONFIG
            = "cors";
    public static final String REST_FILTERS
            = "rest-filters";
    public static final String ASSET_PATH
            = "asset-path";

    public ShbHttpServerType getServerType() {
        if(configMap == null)
            return null;
        return ShbHttpServerType.fromName(
                (String) configMap.get(SERVER_TYPE));
    }

    public String getIP() {
        return getString(HTTP_IP);
    }

    public String getPort() {
        return getString(HTTP_PORT);
    }

    public String getBaseRoute() {
        return getString(HTTP_BASE_ROUTE);
    }

    public String getAssetPath() {
        return getString(ASSET_PATH);
    }

    public ShbSessionConfig getSessionConfig() {
        ShbSessionConfig sessionConfig =
                new ShbSessionConfig();
        sessionConfig.reconfigure(getShbConfig(SESSION_CONFIG));
        return sessionConfig;
    }

    public ShbCORSConfig getCORSConfig() {
        ShbCORSConfig corsConfig =
                new ShbCORSConfig();
        corsConfig.reconfigure(getShbConfig(CORS_CONFIG));
        return corsConfig;
    }

//    public String get() {
//        return getValue();
//    }
//
//    public String get() {
//        return getValue();
//    }
//
//    public String get() {
//        return getValue();
//    }
//
//    public String get() {
//        return getValue();
//    }
//
//    public String get() {
//        return getValue();
//    }

    public URI getBaseUri() {
        return UriBuilder.fromUri("http://"
                .concat(getString(HTTP_IP))
                .concat(":")
                .concat(getString(HTTP_PORT))
                .concat("/")
                .concat(getString(
                        HTTP_BASE_ROUTE)))
                //.port((Integer) configMap.get(HTTP_PORT))
                .build();
    }

    public static class ShbSessionConfig
            extends ShbConfig {
        public static final String SESSION_NAME
                = "session-name";
        public static final String SESSION_CACHE_SIZE
                = "session-cache-size";
        public static final String SESSION_EXPIRE_SECOND
                = "session-expire-second";

        public String getSessionName() {
            return getString(SESSION_NAME);
        }

        public int getSessionCacheSize() {
            return Integer.parseInt(
                    getString(SESSION_CACHE_SIZE));
        }

        public int getSessionExpireSecond() {
            return Integer.parseInt(getString(
                    SESSION_EXPIRE_SECOND
            ));
        }
    }

    public static class ShbCORSConfig
            extends ShbConfig {
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN
                = "access-control-allow-origin";
        public static final String ACCESS_CONTROL_ALLOW_HEADERS
                = "access-control-allow-headers";
        public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS
                = "access-control-allow-credentials";
        public static final String ACCESS_CONTROL_ALLOW_METHODS
                = "access-control-allow-methods";

        public List<String> getAccessControlAllowOrigin() {
            return getList(ACCESS_CONTROL_ALLOW_ORIGIN);
        }

        public List<String> getAccessControlAllowHeaders() {
            return getList(ACCESS_CONTROL_ALLOW_HEADERS);
        }

        public boolean getAccessControlAllowCredentials() {
            return Boolean.getBoolean(getString(
                    ACCESS_CONTROL_ALLOW_CREDENTIALS
            ));
        }

        public List<String> getAccessControlAllowMethods() {
            return getList(ACCESS_CONTROL_ALLOW_METHODS);
        }
    }
}
