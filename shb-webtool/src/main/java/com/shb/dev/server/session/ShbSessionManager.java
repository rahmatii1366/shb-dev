package com.shb.dev.server.session;

import com.shadbarg.secure.ShbSecureException;
import com.shadbarg.secure.cache.ShbCacheProvider;
import com.shadbarg.secure.key.KeyPairAlgorithm;
import com.shadbarg.secure.key.KeyPairMaker;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.config.ShbServerConfig.ShbSessionConfig;
import com.shb.dev.server.role.ShbRoleType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
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
    protected ShbSessionConfig sessionConfig;

    public ShbSessionManager(
            ShbSessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
        cacheProvider =
                ShbCacheProvider.getInstance(
                        key -> createNewSession(),
                        sessionConfig.getSessionCacheSize(),
                        sessionConfig.getSessionExpireSecond());
    }

    public static ShbSessionManager createSessionManager(
            ShbSessionConfig sessionConfig) {
        return new ShbSessionManager(
                sessionConfig);
    }

    public ShbSession retrieveSession(
            HttpHeaders httpHeaders) {
        ShbSession session = null;
        String sessionKey = null;
        try {
            Cookie cookie = httpHeaders.getCookies()
                    .get(sessionConfig.getSessionName());
            if(cookie != null) {
                sessionKey = cookie.getValue();
                if(sessionKey == null || sessionKey.isEmpty())
                    sessionKey = createSessionKey();
                session = (ShbSession) cacheProvider
                        .retrieve(sessionKey);
            } else {
                sessionKey = createSessionKey();
                session = (ShbSession) cacheProvider
                        .retrieve(sessionKey);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        session.setSessionKey(sessionKey);
        return session;
    }

    public NewCookie registerCookie(
            ShbSession session) {
        if(session == null) {
            logger.error("session is null.");
            return null;
        }
        return new NewCookie(
                sessionConfig.getSessionName(),
                session.getSessionKey(), "/", "", null,
                sessionConfig.getSessionExpireSecond(),
                false, false);
    }

    private static ShbSession createNewSession()
            throws ShbSecureException {
        return new ShbSession(
                KeyPairMaker.createKeyPair(
                        KeyPairAlgorithm.RSA_1024),
                ShbRoleType.GUEST
        );
    }

    private static String createSessionKey() {
        return UUID.randomUUID().toString();
    }

}
