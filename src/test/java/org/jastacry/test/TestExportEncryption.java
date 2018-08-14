package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestExportEncryption
{
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * Testcase get java version.
     *
     */
    @Test
    public void testJavaVersion()
    {
        String version = System.getProperty("java.version");
        System.out.println("Version=" + version);
    }

    /**
     * Testcase get max key length.
     * 
     * @throws NoSuchAlgorithmException
     *             in case of export restrictions
     *
     */
    @Test
    public void testMaxKeylength() throws NoSuchAlgorithmException
    {
        int maxlength = Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding");
        System.out.println("MaxAllowedKeyLength=" + maxlength);
        assertEquals("Encryption key unrestricted", Integer.MAX_VALUE, maxlength);
    }

    /**
     * Testcase testAes256.
     *
     */
    @Test
    public void testAes128()
    {
        try
        {
            helperEncryptDecrypt(128);
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException | InvalidAlgorithmParameterException e)
        {
            org.junit.Assert.fail("exception " + e.getLocalizedMessage());
        }
    }

    /**
     * Testcase testAes256.
     *
     */
    @Test
    public void testAes256()
    {
        try
        {
            helperEncryptDecrypt(256);
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException | InvalidAlgorithmParameterException e)
        {
            org.junit.Assert.fail("exception " + e.getLocalizedMessage());
        }
    }

    /**
     * Helper function.
     *
     * @throws NoSuchAlgorithmException on error
     * @throws InvalidKeySpecException on error
     * @throws NoSuchPaddingException on error
     * @throws InvalidKeyException on error
     * @throws InvalidParameterSpecExceptio on errorn
     * @throws UnsupportedEncodingException on error
     * @throws BadPaddingException on errorv
     * @throws IllegalBlockSizeException on error
     * @throws InvalidAlgorithmParameterException on error
     */
    private void helperEncryptDecrypt(final int bitsize) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException
    {
        int bytesize = bitsize / 8;
        System.out.println("key size " + bytesize);
        byte[] aesKey = new byte[bytesize];
        for (int i = 0; i < aesKey.length; ++i)
        {
            aesKey[i] = (byte) i;
        }
        SecretKey secret = new SecretKeySpec(aesKey, "AES");

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
