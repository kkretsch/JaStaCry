package org.jastacry.layer;

/**
 * AES Layer class.
 *
 * @author Kai Kretschmann
 */

public class AesLayer extends AbsCipherLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "AES Layer";

    /**
     * Used algorithm name.
     */
    private final String myALG = "AES/CBC/PKCS5Padding";

    /**
     * Used algorithm name for the key.
     */
    private final String myKeyALG = "AES";

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
