package com.shadbarg.secure.crypt.des;

import com.shadbarg.secure.crypto.CryptoAttribute;
import com.shadbarg.secure.crypto.CryptoMaker;
import com.shadbarg.secure.key.SecretKeyAlgorithm;
import com.shadbarg.secure.key.SecretKeyMaker;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * @author Mohammad Rahmati, 4/16/2017 7:03 PM
 */
public class CryptoMakerTest {
    @Test
    public void testDes() throws Exception {
        SecretKey secretKey = SecretKeyMaker
                .make(SecretKeyAlgorithm.DES);
        byte[] encrypted = CryptoMaker.encrypt(
                "Hello".getBytes("UTF-8"),
                secretKey,
                CryptoAttribute.DES_ECB_PKCS_5_PADDING);
        System.out.println(Hex.encodeHexString(encrypted));
        byte[] decrypted = CryptoMaker.decrypt(encrypted,
                secretKey,
                CryptoAttribute.DES_ECB_PKCS_5_PADDING);
        System.out.println(new String(decrypted, "UTF-8"));
    }
}
