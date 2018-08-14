package org.jastacry.layer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

/**
 * AES Layer class.
 *
 * SPDX-License-Identifier: MIT
 * 
 * @author Kai Kretschmann
 */
public class AesCtrLayer extends AbstractCipherLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "AES CTR Layer";

    /**
     * Used algorithm name.
     */
    private static final String MYALG = "AES/CTR/NoPadding";

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
    private static final int IVLEN = 16;

    /**
     * Salt length.
     */
    private static final int SALTLEN = 0;

    /**
     * Iteration count.
     */
    private static final int COUNT = 0;

    /**
     * Size of key. (16 bytes)
     */
    private static final int KEYSIZE = 128;

    /**
     * Constructor of AesLayer.
     */
    public AesCtrLayer()
    {
        super(AesCtrLayer.class, LAYERNAME);
    }

    /**
     * Generate Keys from plain password.
     *
     * @throws NoSuchAlgorithmException
     *             on error
     * @throws InvalidKeySpecException
     *             on error
     */
    @Override
    protected final void setupPBE() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] key = new String(cPasswd).getBytes(StandardCharsets.UTF_8);
        final MessageDigest sha = MessageDigest.getInstance(MYHASHALG);
        key = sha.digest(key);
        key = Arrays.copyOf(key, KEYSIZE / 8);
        pbeSecretKeySpec = new SecretKeySpec(key, MYKEYALG);
    }

    /**
     * init function. Overrides base init but uses it for setting base values.
     *
     * @param data
     *            to initialize the crypt value.
     */
    @Override
    public final void init(final String data)
    {
        super.init();

        this.cPasswd = data.toCharArray();
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
    protected int getMyIVLen()
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

}
