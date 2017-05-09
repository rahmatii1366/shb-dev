package com.shb.dev.server.session;

import com.shadbarg.secure.ShbSecureException;
import com.shadbarg.secure.cache.ShbCacheProvider;
import com.shadbarg.secure.key.KeyPairAlgorithm;
import com.shadbarg.secure.key.KeyPairMaker;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.config.ShbServerConfig.ShbSessionConfig;
import com.shb.dev.server.role.ShbRoleType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.util.UUID;

/**
 * @author Mohammad Rahmati, 4/29/2017 2:15 PM
 */
public class ShbSessionManager {
    final static Logger logger =
            Logger.getLogger(ShbSessionManager.class);
    public static final String SHB_SESSION_MANAGER =
            "shb-session-manager";
    private ShbCacheProvider cacheProvider = null;

    public ShbSessionManager(
            int cacheSize, int expireSeconds) {
        cacheProvider =
                ShbCacheProvider.getInstance(
                        key -> createNewSession(),
                        cacheSize, expireSeconds);
    }

    public static ShbSessionManager createSessionManager(
            ShbSessionConfig sessionConfig) {
        return new ShbSessionManager(
                (Integer) sessionConfig
                        .getSessionCacheSize(),
                (Integer) sessionConfig
                        .getSessionExpireSecond());
    }

    public ShbSession retrieveSession(
            String sessionKey) {
        try {
            return (ShbSession) cacheProvider
                    .retrieve(sessionKey);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void setSessionCookie(
            MultivaluedMap httpHeader,
            Cookie cookie,
            String sessionName,
            int maxAge) {
        if(cookie != null)
            httpHeader.add("Set-Cookie", new NewCookie(
                    sessionName,
                    cookie.getValue(),
                    "/", null, null,
                    maxAge, false, true)
            );
        else {
            String sessionKey = createSessionKey();
            ShbSession shbSession =
                    retrieveSession(sessionKey);
            httpHeader.add("Set-Cookie", new NewCookie(
                    sessionName,
                    sessionKey,
                    "/", null, null,
                    maxAge, false, true)
            );
        }
    }

    private static ShbSession createNewSession()
            throws ShbSecureException {
        return new ShbSession(
                KeyPairMaker.createKeyPair(
                        KeyPairAlgorithm.RSA_1024),
                ShbRoleType.GUEST
        );
    }

    public static String createSessionKey() {
        return UUID.randomUUID().toString();
    }

}
