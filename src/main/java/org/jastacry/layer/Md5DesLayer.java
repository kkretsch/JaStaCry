package org.jastacry.layer;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class Md5DesLayer extends AbsCipherLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "MD5DES Layer";

    /**
     * Used algorithm name.
     */
    private final String myALG = "PBEWithMD5AndDES";

    /**
     * Salt.
     */
    private final byte[] salt = {(byte) 0xc7, (byte) 0x73, (byte) 0x21,
            (byte) 0x8c, (byte) 0x7e, (byte) 0xc8,
            (byte) 0xee, (byte) 0x99};

    /**
     * Interation count.
     */
    private final int count = 20;

    /**
     * Constructor of Md5DesLayer.
     */
    public Md5DesLayer() {
        super(Md5DesLayer.class);
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
        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);
        final char[] cPasswd = data.toCharArray();
        pbeKeySpec = new PBEKeySpec(cPasswd);
        try {
            keyFac = SecretKeyFactory.getInstance(sALG);
            pbeKey = keyFac.generateSecret(pbeKeySpec);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * Print layer name function.
     *
     * @return Layername as String
     */
    public final String toString() {
        return LAYERNAME;
    }

}
