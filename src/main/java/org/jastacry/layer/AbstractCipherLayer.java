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
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public abstract class AbstractCipherLayer extends AbstractBasicLayer
{

    /**
     * Block size.
     */
    private static final int ONEBLOCKSIZE = 256;

    /**
     * How many bits in a byte.
     */
    protected static final int BITSPERBYTE = 8;

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
    protected String sAlg;

    /**
     * Algorithm for the key.
     */
    protected String sKeyAlg;

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
    protected int iIvLen;

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
     * @param layerName
     *            name of real layer
     */
    public AbstractCipherLayer(final Class<?> cClass, final String layerName)
    {
        super(cClass, layerName);
    }

    /**
     * Abstract base method for getting algorithm name back.
     * @return String
     */
    protected abstract String getMyAlg();

    /**
     * Abstract base method for getting key algorithm name back.
     * @return String
     */
    protected abstract String getMyKeyAlg();

    /**
     * Abstract base method for getting salt len back.
     * @return int length
     */
    protected abstract int getMySaltLen();

    /**
     * Abstract base method for getting IV length back.
     * @return int length
     */
    protected abstract int getMyIvLen();

    /**
     * Abstract base method for getting a counter back.
     * @return int
     */
    protected abstract int getMyCount();

    /**
     * Abstract base method for getting key size back.
     * @return int length
     */
    protected abstract int getMyKeysize();

    /**
     * Generate random salt.
     */
    protected final void getSalt()
    {
        salt = new byte[iSaltLen];
        new SecureRandom().nextBytes(salt);
    }

    /**
     * store IV bytes.
     */
    protected final void getIv()
    {
        ivBytes = new byte[iIvLen];
    }

    /**
     * Set base values via own getters, which are defined in child classes.
     */
    protected final void init()
    {
        this.sAlg = getMyAlg();
        this.sKeyAlg = getMyKeyAlg();
        this.iSaltLen = getMySaltLen();
        this.iIvLen = getMyIvLen();
        this.iCount = getMyCount();
        this.iKeysize = getMyKeysize();
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws NoSuchAlgorithmException
     *             on error
     * @throws InvalidKeySpecException
     *             on error
     */
    protected abstract void setupPbe() throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * encode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        Cipher pbeCipher;
        try
        {
            logger.debug("encoding");
            getSalt();
            logger.debug("got salt");
            setupPbe();
            logger.debug("made key");

            pbeCipher = Cipher.getInstance(sAlg);
            logger.debug("cipher created");
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec);
            logger.debug("cipher initialized");

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            { // NOPMD by kai on 21.11.17 17:34
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            if (0 == iIvLen)
            {
                logger.trace("No IV to write");
            }
            else
            {
                ivBytes = pbeCipher.getIV();

                if (null == ivBytes)
                {
                    logger.error("IV empty");
                }
                else
                {
                    outputStream.write(ivBytes, 0, iIvLen);
                } // if
            } // if

            outputStream.write(salt, 0, iSaltLen);
            outputStream.write(ciphertext);
            logger.info("close pipe");
            outputStream.close();
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | InvalidKeySpecException | BadPaddingException e)
        {
            logger.catching(e);
        }

    }

    /**
     * decode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        // Create PBE Cipher
        Cipher pbeCipher;
        int iReadBytes;
        try
        {
            if (0 == iIvLen)
            {
                logger.trace("No IV to read");
            }
            else
            {
                ivBytes = new byte[iIvLen];
                iReadBytes = inputStream.read(ivBytes, 0, iIvLen);
                if (iIvLen != iReadBytes)
                {
                    logger.error("read {} bytes of IV, expecting {}.", iReadBytes, iIvLen);
                } // if
            } // if

            salt = new byte[iSaltLen];
            iReadBytes = inputStream.read(salt, 0, iSaltLen);
            if (iSaltLen != iReadBytes)
            {
                logger.error("read {} bytes of salt, expecting {}.", iReadBytes, iSaltLen);
            } // if

            // call implementation of child class method.
            setupPbe();

            pbeCipher = Cipher.getInstance(sAlg);
            if (0 == iIvLen)
            {
                pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec);
            }
            else
            {
                pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, new IvParameterSpec(ivBytes));
            } // if

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            { // NOPMD by kai on 21.11.17 17:34
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            outputStream.write(ciphertext);
            logger.info("close pipe");
            outputStream.close();
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException e)
        {
            logger.catching(e);
        }
    }

    @Override
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }

}
