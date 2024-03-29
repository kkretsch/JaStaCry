package org.jastacry.layer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.jastacry.JastacryException;

/**
 * MD5 DES Layer class.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class Md5DesLayer extends AbstractCipherLayer
{
    /**
     * static name of the layer.
     */
    static final String LAYERNAME = "MD5DES Layer";

    /**
     * Used algorithm name.
     */
    private static final String MYALG = "DESede/CBC/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private static final String MYKEYALG = "DESede";

    /**
     * Used algorithm name for the hash.
     */
    private static final String MYHASHALG = "md5";

    /**
     * IV length.
     */
    private static final int IVLEN = 8;

    /**
     * Salt length.
     */
    private static final int SALTLEN = 8;

    /**
     * Iteration count.
     */
    private static final int COUNT = 20;

    /**
     * Size of key.
     */
    private static final int KEYSIZE = 24;

    /**
     * local key storage implementation.
     */
    private byte[] keyBytes;

    /**
     * Constructor of Md5DesLayer.
     */
    public Md5DesLayer()
    {
        super(Md5DesLayer.class, LAYERNAME);
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws JastacryException on error
     */
    @Override
    protected final void setupPbe() throws JastacryException
    {
        logger.debug("in child setupPBE");
        pbeKey = new SecretKeySpec(keyBytes, MYKEYALG);
        pbeSecretKeySpec = new SecretKeySpec(pbeKey.getEncoded(), strKeyAlg);
    }

    /**
     * init function.
     *
     * @param data to initialize the crypt value.
     */
    @Override
    public final void init(final String data)
    {
        super.init();

        this.chPasswd = data.toCharArray();
        try
        {
            final var msgDigest = MessageDigest.getInstance(MYHASHALG);
            final byte[] digestOfPassword = msgDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            keyBytes = Arrays.copyOf(digestOfPassword, KEYSIZE);
        }
        catch (final NoSuchAlgorithmException e)
        {
            logger.catching(e);
        }
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
        if (!(o instanceof Md5DesLayer))
        {
            return false;
        }

        final Md5DesLayer layer = (Md5DesLayer) o;
        return Arrays.equals(layer.keyBytes, this.keyBytes);
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Arrays.hashCode(keyBytes);
    }
}
