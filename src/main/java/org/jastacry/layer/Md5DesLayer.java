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
    public final static String LAYERNAME = "MD5DES Layer";
    final String myALG = "PBEWithMD5AndDES";

    // Salt
    private final byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8,
            (byte) 0xee, (byte) 0x99 };
    // Iteration count
    private final int count = 20;

    public Md5DesLayer() {
        super(Md5DesLayer.class);
    }

    @Override
    public void init(final String data) {
        this.ALG = myALG;
        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);
        final char[] cPasswd = data.toCharArray();
        pbeKeySpec = new PBEKeySpec(cPasswd);
        try {
            keyFac = SecretKeyFactory.getInstance(ALG);
            pbeKey = keyFac.generateSecret(pbeKeySpec);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return LAYERNAME;
    }

}
