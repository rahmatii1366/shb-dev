package com.shb.dev.server.session;

import com.shadbarg.secure.crypto.CryptoAttribute;
import com.shadbarg.secure.crypto.CryptoMaker;
import com.shb.dev.server.role.ShbRoleType;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * @author Mohammad Rahmati, 4/18/2017 4:30 PM
 */
public class ShbSession {
    private KeyPair keyPair;
    private ShbRoleType roleType;
    private String sessionKey;

    ShbSession(KeyPair keyPair,
               ShbRoleType roleType) {
        this.keyPair = keyPair;
        this.roleType = roleType;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public ShbRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(ShbRoleType roleType) {
        this.roleType = roleType;
    }

    public byte[] getPublicKeyBytes() {
        return keyPair.getPublic().getEncoded();
    }

    public byte[] decrypt(byte[] rawMessage)
            throws Exception {
        return CryptoMaker.decrypt(rawMessage,
                keyPair.getPrivate(),
                CryptoAttribute.RSA);
    }
}
