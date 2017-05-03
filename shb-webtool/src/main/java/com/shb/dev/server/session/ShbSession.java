package com.shb.dev.server.session;

import com.shadbarg.secure.crypto.CryptoAttribute;
import com.shadbarg.secure.crypto.CryptoMaker;
import com.shb.dev.server.role.ShbRoleType;

import java.security.KeyPair;

/**
 * @author Mohammad Rahmati, 4/18/2017 4:30 PM
 */
public class ShbSession {
    private KeyPair keyPair;
    private ShbRoleType roleType;

    ShbSession(KeyPair keyPair,
               ShbRoleType roleType) {
        this.keyPair = keyPair;
        this.roleType = roleType;
    }

    public ShbRoleType getRole()
            throws Exception {
        return roleType;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public ShbRoleType getRoleType() {
        return roleType;
    }

    void setRoleType(ShbRoleType roleType) {
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
