package org.jastacry.layer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jastacry.JastacryException;

/**
 * Abstract base class for encryption.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
abstract class AbstractCipherLayer extends AbstractBasicLayer
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
     * Optional additional parameters depending on algorithm.
     */
    protected AlgorithmParameterSpec optParams;
    /**
     * ALG for the data.
     */
    private String strAlg;

    /**
     * Reuse SecureRandom object.
     */
    private SecureRandom secrand = new SecureRandom();

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
    private int currentIvLen;

    /**
     * IV bytes.
     */
    private byte[] ivBytes;

    /**
     * Salt length.
     */
    private int currentSaltLen;

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
    AbstractCipherLayer(final Class<?> cClass, final String layerName)
    {
        super(cClass, layerName);
        optParams = null;
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
    private void getSalt()
    {
        salt = new byte[currentSaltLen];
        secrand.nextBytes(salt);
    }

    /**
     * Generate random bytes for i.e.IV.
     * @param a
     */
    protected void setRandom(byte[] a) {
        secrand.nextBytes(a);
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
     * @throws JastacryException on error
     */
    protected abstract void setupPbe() throws JastacryException;

    /**
     * Write IV data if any.
     * @param outputStream stream to write to
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
                logger.trace("did write {} IV bytes to stream", currentIvLen);
            } // if
        } // if
    }

    protected void setIv(byte[] a)
    {
        this.ivBytes = a;
    }

    protected byte[] getIv()
    {
        return this.ivBytes;
    }

    /**
     * Read IV data if any.
     * @param inputStream stream to read from
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
            iReadBytes = readAllBytes(inputStream, ivBytes, currentIvLen);
            if (currentIvLen != iReadBytes)
            {
                logger.error("read {} bytes of IV, expecting {}.", iReadBytes, currentIvLen);
            }
            else
            {
                logger.trace("did read {} IV bytes from stream", iReadBytes);
            }
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
        logger.trace("did write {} salt bytes to stream", currentSaltLen);
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
        iReadBytes = readAllBytes(inputStream, salt, currentSaltLen);
        if (currentSaltLen != iReadBytes)
        {
            logger.error("read {} bytes of salt, expecting {}.", iReadBytes, currentSaltLen);
        }
        else
        {
            logger.trace("did read {} salt bytes from stream", iReadBytes);
        }
    }

    /**
     * encode Stream function.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        Cipher pbeCipher;
        try
        {
            getSalt();
            setupPbe();

            pbeCipher = Cipher.getInstance(strAlg);
            if(optParams != null) {
                pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec, optParams);
            } else {
                pbeCipher.init(Cipher.ENCRYPT_MODE, pbeSecretKeySpec);
            }

            final var buffer = new ByteArrayOutputStream();

            int nRead;
            final var data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            {
                buffer.write(data, 0, nRead);
                progress(nRead);
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
                | JastacryException | BadPaddingException | IOException | InvalidAlgorithmParameterException e)
        {
            logger.catching(e);
            throw (JastacryException) new JastacryException("encStream failed").initCause(e);
        }

    }

    /**
     * decode Stream function.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
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
                if(null != optParams) {
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, optParams);
                } else {
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec);
                }
            }
            else
            {
                if(null != optParams) {
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, optParams);
                } else {
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeSecretKeySpec, new IvParameterSpec(ivBytes));
                }
            } // if

            final var buffer = new ByteArrayOutputStream();

            var nRead = 0;
            final var data = new byte[ONEBLOCKSIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            {
                buffer.write(data, 0, nRead);
                progress(nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the clear text
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            outputStream.write(ciphertext);
            outputStream.close();
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | JastacryException | IOException e)
        {
            logger.catching(e);
            throw (JastacryException) new JastacryException("decStream failed").initCause(e);
        }
    }

    @Override
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        throw new UnsupportedOperationException();
    }

}
