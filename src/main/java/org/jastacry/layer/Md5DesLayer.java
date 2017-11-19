package org.jastacry.layer;

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
    private final String myALG = "RSA/ECB/PKCS1Padding";

    /**
     * Used algorithm name for the key.
     */
    private final String myKeyALG = "Blowfish";

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
    private static final int KEYSIZE = 64;


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
        this.sKeyALG = myKeyALG;
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
