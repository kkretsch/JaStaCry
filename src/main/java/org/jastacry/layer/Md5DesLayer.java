package org.jastacry.layer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

/**
 * MD5 DES Layer class.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class Md5DesLayer extends AbstractCipherLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "MD5DES Layer";

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
    private byte[] keyBytes; // NOPMD by kkretsch on 29.03.18 14:53

    /**
     * Constructor of Md5DesLayer.
     */
    public Md5DesLayer() {
        super(Md5DesLayer.class, LAYERNAME);
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
        logger.debug("in child setupPBE");
        pbeKey = new SecretKeySpec(keyBytes, MYKEYALG);
        pbeSecretKeySpec = new SecretKeySpec(pbeKey.getEncoded(), sKeyALG);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize the crypt value.
     */
    @Override
    public final void init(final String data) {
        super.init();

        this.cPasswd = data.toCharArray();
        try {
            final MessageDigest msgDigest = MessageDigest.getInstance(MYHASHALG);
            final byte[] digestOfPassword = msgDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            keyBytes = Arrays.copyOf(digestOfPassword, KEYSIZE);
        } catch (final NoSuchAlgorithmException e) {
            logger.catching(e);
        }
    }


    @Override
    protected final String getMyAlg() {
        return MYALG;
    }

    @Override
    protected final String getMyKeyAlg() {
        return MYKEYALG;
    }

    @Override
    protected int getMySaltLen() {
        return SALTLEN;
    }

    @Override
    protected int getMyIVLen() {
        return IVLEN;
    }

    @Override
    protected int getMyCount() {
        return COUNT;
    }

    @Override
    protected int getMyKeysize() {
        return KEYSIZE;
    }
}
