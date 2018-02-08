package org.jastacry.layer;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Layer class.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class AesLayer extends AbstractCipherLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "AES Layer";

    /**
     * Used algorithm name.
     */
    private static final String MYALG = "AES/CBC/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private static final String MYKEYALG = "AES";

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
    public AesLayer() {
        super(AesLayer.class);
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
    protected final void setupPBE() throws NoSuchAlgorithmException, InvalidKeySpecException {
        keyFac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        pbeKeySpec = new PBEKeySpec(cPasswd, salt, iCount, iKeysize);
        pbeKey = keyFac.generateSecret(pbeKeySpec);
        pbeSecretKeySpec = new SecretKeySpec(pbeKey.getEncoded(), sKeyALG);
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize the crypt value.
     */
    public final void init(final String data) {
        this.sALG = MYALG;
        this.sKeyALG = MYKEYALG;
        this.iSaltLen = SALTLEN;
        this.iIVLen = IVLEN;
        this.iCount = COUNT;
        this.iKeysize = KEYSIZE;
        this.cPasswd = data.toCharArray();
    }

    @Override
    /**
     * Print layer name function.
     *
     * @return Layer name as String
     */
    public final String toString() {
        return LAYERNAME;
    }

}
