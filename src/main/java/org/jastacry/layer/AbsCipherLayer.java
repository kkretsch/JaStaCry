package org.jastacry.layer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

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
    private static final int ONEBLOCKSIZE = 1024;
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
     * ALG.
     */
    protected String sALG;

    /**
     * Constructor of abstract class.
     *
     * @param c class name of calling class
     */
    public AbsCipherLayer(final Class<?> c) {
        super(c);
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
        // Create PBE Cipher
        Cipher pbeCipher;
        try {
            pbeCipher = Cipher.getInstance(sALG);
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the cleartext
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            os.write(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
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
            pbeCipher = Cipher.getInstance(sALG);
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            final byte[] data = new byte[ONEBLOCKSIZE];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            final byte[] bInput = buffer.toByteArray();

            // Encrypt the cleartext
            final byte[] ciphertext = pbeCipher.doFinal(bInput);
            os.write(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

}
