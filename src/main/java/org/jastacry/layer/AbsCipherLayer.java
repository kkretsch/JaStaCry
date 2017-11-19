package org.jastacry.layer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract base class for encryption.
 *
 * @author Kai Kretschmann
 *
 */
public abstract class AbsCipherLayer extends AbsLayer {

    /**
     * Block size.
     */
    private static final int ONEBLOCKSIZE = 256;

    /**
     * PBEKeySpec.
     */
    protected PBEKeySpec pbeKeySpec;

    /**
     * PBEParameterSpec.
     */
    protected PBEParameterSpec pbeParamSpec;

    /**
     * SecretKeyFactory.
     */
    protected SecretKeyFactory keyFac;

    /**
     * SecretKey.
     */
    protected SecretKey pbeKey;

    /**
     * SecretKeySpec.
     */
    protected SecretKeySpec pbeSecretKeySpec;

    /**
     * ALG for the data.
     */
    protected String sALG;

    /**
     * Algorithm for the key.
     */
    protected String sKeyALG;

    /**
     * char array of password.
     */
    protected char[] cPasswd;

    /**
     * Iterations count as defined by child class.
     */
    protected int iCount;

    /**
     * Key size as defined by child class.
     */
    protected int iKeysize;

    /**
     * IV length.
     */
    protected int iIVLen;

    /**
     * IV bytes.
     */
    protected byte[] ivBytes;

    /**
     * Salt length.
     */
    protected int iSaltLen;

    /**
     * salt.
     */
    protected byte[] salt;

    /**
     * Constructor of abstract class.
     *
     * @param c class name of calling class
     */
    public AbsCipherLayer(final Class<?> c) {
        super(c);
    }

    /**
     * Generate random salt.
     */
    protected void getSalt() {
        salt = new byte[iSaltLen];
        new SecureRandom().nextBytes(salt);
    }

    /**
     * store IV bytes.
     */
    protected void getIV() {
        ivBytes = new byte[iIVLen];
    }

    /**
     * Generate Keys from plain password.
     * @throws NoSuchAlgorithmException on error
     * @throws InvalidKeySpecException on error
     */
    protected void setupPBE()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        keyFac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        pbeKeySpec = new PBEKeySpec(cPasswd, salt, iCount, iKeysize);
        pbeKey = keyFac.generateSecret(pbeKeySpec);
        pbeSecretKeySpec = new SecretKeySpec(pbeKey.getEncoded(), sKeyALG);
    }

    @Override
    /**
     * encode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void encStream(final InputStream is, final OutputStream os)
            throws IOException {
        Cipher pbeCipher;
        try {
            getSalt();
            setupPBE();

            pbeCipher = Cipher.getInstance(sALG);
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            ivBytes = pbeCipher.getIV();

            if (null != ivBytes) {
                os.write(ivBytes, 0, iIVLen);
            } // if
            os.write(salt, 0, iSaltLen);
            os.write(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.catching(e);
        } catch (InvalidKeySpecException e) {
            logger.catching(e);
        }

    }

    @Override
    /**
     * decode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void decStream(final InputStream is, final OutputStream os)
            throws IOException {
        // Create PBE Cipher
        Cipher pbeCipher;
        try {
            ivBytes = new byte[iIVLen];
            is.read(ivBytes, 0, iIVLen);
            salt = new byte[iSaltLen];
            is.read(salt, 0, iSaltLen);
            setupPBE();

            pbeCipher = Cipher.getInstance(sALG);
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, new IvParameterSpec(ivBytes));

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            os.write(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.catching(e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.catching(e);
        } catch (InvalidKeySpecException e) {
            logger.catching(e);
        }
    }

}
