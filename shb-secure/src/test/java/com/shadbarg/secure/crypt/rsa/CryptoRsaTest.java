package com.shadbarg.secure.crypt.rsa;

import com.shadbarg.secure.crypto.CryptoAttribute;
import com.shadbarg.secure.crypto.CryptoMaker;
import com.shadbarg.secure.key.KeyPairAlgorithm;
import com.shadbarg.secure.key.KeyPairMaker;
import com.shadbarg.secure.util.ByteStringConverter;
import com.shadbarg.secure.util.HexConverter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyPair;

/**
 * @author Mohammad Rahmati, 4/24/2017 4:50 PM
 */
public class CryptoRsaTest {
    private static KeyPair keyPair = null;
    static byte[] RAW_BYTES;

    @BeforeClass
    public static void beforeClass()
            throws Exception {
        keyPair = KeyPairMaker
                .createKeyPair(
                        KeyPairAlgorithm.RSA_1024);
        RAW_BYTES = new byte[] {9, 8, 7, 6, 5, 4, 3, 2, 1};
    }

    @Test
    public void encrypt()
            throws Exception {
        byte[] encrypted = CryptoMaker.encrypt(RAW_BYTES,
                keyPair.getPublic(),
                CryptoAttribute.RSA);
        byte[] decrypted = CryptoMaker.decrypt(
                encrypted,
                keyPair.getPrivate(),
                CryptoAttribute.RSA);

        Assert.assertEquals(
                HexConverter.toHexString(RAW_BYTES),
                HexConverter.toHexString(decrypted));
    }
}
