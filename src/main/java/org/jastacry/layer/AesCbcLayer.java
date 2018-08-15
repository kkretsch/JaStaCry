package org.jastacry.layer;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Layer class.
 *
 * <p>
 * SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class AesCbcLayer extends AbstractCipherLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "AES CBC Layer";

    /**
     * Used algorithm name.
     */
    private static final String MYALG = "AES/CBC/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private static final String MYKEYALG = "AES";

    /**
     * Used algorithm name for the key factory.
     */
    private static final String MYKEYFACT = "PBKDF2WithHmacSHA1";

    /**
     * IV length.
     */
    private static final int IVLEN = 16;

    /**
     * Salt length.
     */
    private static final int SALTLEN = 32;

    /**
     * Iteration count.
     */
    private static final int COUNT = 10000;

    /**
     * Size of key.
     */
    private static final int KEYSIZE = 128;

    /**
     * Constructor of AesLayer.
     */
    public AesCbcLayer()
    {
        super(AesCbcLayer.class, LAYERNAME);
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
    protected final void setupPbe() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        keyFac = SecretKeyFactory.getInstance(MYKEYFACT);
        pbeKeySpec = new PBEKeySpec(chPasswd, salt, iterCount, currentKeysize);
        pbeKey = keyFac.generateSecret(pbeKeySpec);
        pbeSecretKeySpec = new SecretKeySpec(pbeKey.getEncoded(), strKeyAlg);
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

}
