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
    protected String strAlg;

    /**
     * Algorithm for the key.
     */
    protected String strKeyAlg;

    /**
     * char array of password.
     */
    protected char[] chPasswd;

    /**
     * Iterations count as defined by child class.
     */
    protected int iterCount;

    /**
     * Key size as defined by child class.
     */
    protected int currentKeysize;

    /**
     * IV length.
     */
    protected int currentIvLen;

    /**
     * IV bytes.
     */
    protected byte[] ivBytes;

    /**
     * Salt length.
     */
    protected int currentSaltLen;

    /**
     * salt.
     */
    protected byte[] salt;

    /**
     * Constructor of abstract class.
     *
     * @param cClass class name of calling class
     * @param layerName name of real layer
     */
    public AbstractCipherLayer(final Class<?> cClass, final String layerName)
    {
        super(cClass, layerName);
    }

    /**
     * Abstract base method for getting algorithm name back.
     *
     * @return String
     */
    protected abstract String getMyAlg();

    /**
     * Abstract base method for getting key algorithm name back.
     *
     * @return String
     */
    protected abstract String getMyKeyAlg();

    /**
     * Abstract base method for getting salt len back.
     *
     * @return int length
     */
    protected abstract int getMySaltLen();

    /**
     * Abstract base method for getting IV length back.
     *
     * @return int length
     */
    protected abstract int getMyIvLen();

    /**
     * Abstract base method for getting a counter back.
     *
     * @return int
     */
    protected abstract int getMyCount();

    /**
     * Abstract base method for getting key size back.
     *
     * @return int length
     */
    protected abstract int getMyKeysize();

    /**
     * Generate random salt.
     */
    protected final void getSalt()
    {
        salt = new byte[currentSaltLen];
        new SecureRandom().nextBytes(salt);
    }

    /**
     * store IV bytes.
     */
    protected final void getIv()
    {
        ivBytes = new byte[currentIvLen];
    }

    /**
     * Set base values via own getters, which are defined in child classes.
     */
    protected final void init()
    {
        this.strAlg = getMyAlg();
        this.strKeyAlg = getMyKeyAlg();
        this.currentSaltLen = getMySaltLen();
        this.currentIvLen = getMyIvLen();
        this.iterCount = getMyCount();
        this.currentKeysize = getMyKeysize();
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws NoSuchAlgorithmException on error
     * @throws InvalidKeySpecException on error
     */
    protected abstract void setupPbe() throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Write IV data if any.
     * @param pbeCipher the cipher object
     * @throws IOException in case of error
     */
    private void writeIv(final OutputStream outputStream, final Cipher pbeCipher) throws IOException
    {
        if (0 == currentIvLen)
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
                outputStream.write(ivBytes, 0, currentIvLen);
            } // if
        } // if        
    }

    /**
     * Read IV data if any.
     * @throws IOException in case of error
     */
    private void readIv(final InputStream inputStream) throws IOException
    {
        int iReadBytes;
        if (0 == currentIvLen)
        {
            logger.trace("No IV to read");
        }
        else
        {
            ivBytes = new byte[currentIvLen];
            iReadBytes = inputStream.read(ivBytes, 0, currentIvLen);
            if (currentIvLen != iReadBytes)
            {
                logger.error("read {} bytes of IV, expecting {}.", iReadBytes, currentIvLen);
            } // if
        } // if
    }

    /**
     * Write salt to stream.
     * @param outputStream to write to
     * @throws IOException in case of error
     */
    private void writeSalt(final OutputStream outputStream) throws IOException
    {
        outputStream.write(salt, 0, currentSaltLen);        
    }

    /**
     * Read salt from stream.
     * @param inputStream to read from
     * @throws IOException in case of error
     */
    private void readSalt(final InputStream inputStream) throws IOException
    {
        int iReadBytes;
        salt = new byte[currentSaltLen];
        iReadBytes = inputStream.read(salt, 0, currentSaltLen);
        if (currentSaltLen != iReadBytes)
        {
            logger.error("read {} bytes of salt, expecting {}.", iReadBytes, currentSaltLen);
        } // if
    }

    /**
     * encode Stream function.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws IOException thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        Cipher pbeCipher;
        try
        {
            getSalt();
            setupPbe();

            pbeCipher = Cipher.getInstance(strAlg);
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            writeIv(outputStream, pbeCipher);
            writeSalt(outputStream);

            outputStream.write(ciphertext);
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
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws IOException thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        // Create PBE Cipher
        Cipher pbeCipher;
        try
        {
            readIv(inputStream);
            readSalt(inputStream);

            // call implementation of child class method.
            setupPbe();

            pbeCipher = Cipher.getInstance(strAlg);
            if (0 == currentIvLen)
            {
                pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec);
            }
            else
            {
                pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, new IvParameterSpec(ivBytes));
            } // if

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead = 0;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            outputStream.write(ciphertext);
            outputStream.close();
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException e)
        {
            logger.catching(e);
        }
    }

    @Override
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        throw new UnsupportedOperationException();
    }

}
