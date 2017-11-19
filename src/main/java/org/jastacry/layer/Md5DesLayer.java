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
 * @author Kai Kretschmann
 */

public class Md5DesLayer extends AbsCipherLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "MD5DES Layer";

    /**
     * Used algorithm name.
     */
    private final String myALG = "DESede/CBC/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private final String myKeyALG = "DESede";

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
    public Md5DesLayer() {
        super(Md5DesLayer.class);
    }

    /**
     * Generate Keys from plain password.
     * @throws NoSuchAlgorithmException on error
     * @throws InvalidKeySpecException on error
     */
    @Override
    protected void setupPBE()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        logger.debug("in child setupPBE");
        pbeKey = new SecretKeySpec(keyBytes, "DESede");
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
        this.sALG = myALG;
        this.sKeyALG = myKeyALG;
        this.iSaltLen = SALTLEN;
        this.iIVLen = IVLEN;
        this.iCount = COUNT;
        this.iKeysize = KEYSIZE;
        this.cPasswd = data.toCharArray();
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(data.getBytes(StandardCharsets.UTF_8));
            keyBytes = Arrays.copyOf(digestOfPassword, KEYSIZE);
        } catch (NoSuchAlgorithmException e) {
            logger.catching(e);
        }
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
