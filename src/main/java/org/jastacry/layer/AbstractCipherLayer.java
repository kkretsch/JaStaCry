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
import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract base class for encryption.
 *
 * @author Kai Kretschmann
 *
 */
public abstract class AbstractCipherLayer extends AbstractLayer {

    /**
     * Block size.
     */
    private static final int ONEBLOCKSIZE = 256;

    /**
     * PBEKeySpec.
     */
    protected PBEKeySpec pbeKeySpec;

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
     * @param cClass
     *            class name of calling class
     */
    public AbstractCipherLayer(final Class<?> cClass) {
        super(cClass);
    }

    /**
     * Generate random salt.
     */
    protected final void getSalt() {
        salt = new byte[iSaltLen];
        new SecureRandom().nextBytes(salt);
    }

    /**
     * store IV bytes.
     */
    protected final void getIV() {
        ivBytes = new byte[iIVLen];
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws NoSuchAlgorithmException
     *             on error
     * @throws InvalidKeySpecException
     *             on error
     */
    protected abstract void setupPBE() throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * encode Stream function.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        Cipher pbeCipher;
        try {
            logger.debug("encoding");
            getSalt();
            logger.debug("got salt");
            setupPBE();
            logger.debug("made key");

            pbeCipher = Cipher.getInstance(sALG);
            logger.debug("cipher created");
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec);
            logger.debug("cipher initialized");

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) { // NOPMD by kai on 21.11.17 17:34
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            ivBytes = pbeCipher.getIV();

            if (null == ivBytes) {
                logger.error("IV empty");
            } else {
                outputStream.write(ivBytes, 0, iIVLen);
            } // if
            outputStream.write(salt, 0, iSaltLen);
            outputStream.write(ciphertext);
            logger.info("close pipe");
            outputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | InvalidKeySpecException | BadPaddingException e) {
            logger.catching(e);
        }

    }

    /**
     * decode Stream function.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        // Create PBE Cipher
        Cipher pbeCipher;
        try {
            ivBytes = new byte[iIVLen];
            int iReadBytes = inputStream.read(ivBytes, 0, iIVLen);
            if (iIVLen != iReadBytes) {
                logger.error("read {} bytes of IV, expecting {}.", iReadBytes, iIVLen);
            } // if

            salt = new byte[iSaltLen];
            iReadBytes = inputStream.read(salt, 0, iSaltLen);
            if (iSaltLen != iReadBytes) {
                logger.error("read {} bytes of salt, expecting {}.", iReadBytes, iSaltLen);
            } // if

            // call implementation of child class method.
            setupPBE();

            pbeCipher = Cipher.getInstance(sALG);
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, new IvParameterSpec(ivBytes));

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) { // NOPMD by kai on 21.11.17 17:34
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            outputStream.write(ciphertext);
            logger.info("close pipe");
            outputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
            logger.catching(e);
        }
    }

}
