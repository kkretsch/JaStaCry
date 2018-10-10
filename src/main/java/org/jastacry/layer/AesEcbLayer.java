package org.jastacry.layer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.jastacry.JastacryException;

/**
 * AES Layer class.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class AesEcbLayer extends AbstractCipherLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "AES ECB Layer";

    /**
     * Used algorithm name.
     */
    private static final String MYALG = "AES/ECB/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private static final String MYKEYALG = "AES";

    /**
     * Used algorithm name for the key hash.
     */
    private static final String MYHASHALG = "SHA-256";

    /**
     * IV length.
     */
    private static final int IVLEN = 0;

    /**
     * Salt length.
     */
    private static final int SALTLEN = 0;

    /**
     * Iteration count.
     */
    private static final int COUNT = 0;

    /**
     * Size of key.
     */
    private static final int KEYSIZE = 128;

    /**
     * Constructor of AesLayer.
     */
    public AesEcbLayer()
    {
        super(AesEcbLayer.class, LAYERNAME);
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws JastacryException on error
     */
    @Override
    protected final void setupPbe() throws JastacryException
    {
        MessageDigest sha;
        try
        {
            sha = MessageDigest.getInstance(MYHASHALG);
            final byte[] keyPlaintext = new String(chPasswd).getBytes(StandardCharsets.UTF_8);
            final byte[] keyDigest = sha.digest(keyPlaintext);
            final byte[] keyDigestTrimmed = Arrays.copyOf(keyDigest, KEYSIZE / BITSPERBYTE);

            pbeSecretKeySpec = new SecretKeySpec(keyDigestTrimmed, MYKEYALG);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw (JastacryException) new JastacryException("Setup PBE failed").initCause(e);
        }

    }

    /**
     * init function. Overrides base init but uses it for setting base values.
     *
     * @param data to initialize the crypt value.
     */
    @Override
    public final void init(final String data)
    {
        super.init();

        this.chPasswd = data.toCharArray();
    }

    @Override
    protected final String getMyAlg()
    {
        return MYALG;
    }

    @Override
    protected final String getMyKeyAlg()
    {
        return MYKEYALG;
    }

    @Override
    protected int getMySaltLen()
    {
        return SALTLEN;
    }

    @Override
    protected int getMyIvLen()
    {
        return IVLEN;
    }

    @Override
    protected int getMyCount()
    {
        return COUNT;
    }

    @Override
    protected int getMyKeysize()
    {
        return KEYSIZE;
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof AesEcbLayer))
        {
            return false;
        }

        final AesEcbLayer layer = (AesEcbLayer) o;
        return Arrays.equals(layer.chPasswd, this.chPasswd);
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return java.util.Arrays.hashCode(chPasswd);
    }
}
