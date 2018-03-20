package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestExportEncryption {
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";
    private final char[] password = "myPassword".toCharArray();
    private final byte[] salt = {0x01, 0x02, 0x03};


    /**
     * Testcase get java version.
     *
     */
    @Test
    public void testJavaVersion() {
        String version = System.getProperty("java.version");
        System.out.println("Version=" + version);
    }

    /**
     * Testcase get max key length.
     * @throws NoSuchAlgorithmException 
     *
     */
    @Test
    public void testMaxKeylength() throws NoSuchAlgorithmException {
        int maxlength = Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding");
        System.out.println("MaxAllowedKeyLength=" + maxlength);
        assertEquals("Encryption key unrestricted", Integer.MAX_VALUE, maxlength); 
    }

    /**
     * Testcase testAes256.
     *
     */
    @Test
    public void testAes128() {
        try {
            helperEncryptDecrypt(65536, 128);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testcase testAes256.
     *
     */
    @Test
    public void testAes256() {
        try {
            helperEncryptDecrypt(65536, 256);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function.
     *
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchPaddingException 
     * @throws InvalidKeyException 
     * @throws InvalidParameterSpecException 
     * @throws UnsupportedEncodingException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidAlgorithmParameterException 
     */
    private void helperEncryptDecrypt(final int iterations, final int bitsize) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, iterations, bitsize);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(testdata.getBytes("UTF-8"));
        System.out.println(ciphertext.length);

        /* Decrypt the message, given derived key and initialization vector. */
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
        System.out.println(plaintext);

        assertEquals("Message content equal", testdata, plaintext);
    }
}
