package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Very simple algorithm just to infuse some more complex data rotation.
 *
 * @author Kai Kretschmann
 */

public class XorLayer extends AbsLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Xor Layer";
    /**
     * private byte mask to xor with.
     */
    private byte bMask;

    /**
     * Constructor of XorLayer.
     */
    public XorLayer() {
        super(XorLayer.class);
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize the xor value.
     */
    public final void init(final String data) {
        this.bMask = (byte) Integer.parseInt(data);
    }

    @Override
    /**
     * encode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void encStream(final InputStream is, final OutputStream os)
            throws IOException {
        int iChar;
        byte bChar;
        while ((iChar = is.read()) != -1) {
            bChar = (byte) iChar;
            bChar = (byte) (bChar ^ this.bMask);
            os.write(bChar);
        }
    }

    @Override
    /**
     * decode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void decStream(final InputStream is, final OutputStream os)
            throws IOException {
        int iChar;
        byte bChar;
        while ((iChar = is.read()) != -1) {
            bChar = (byte) iChar;
            bChar = (byte) (bChar ^ this.bMask);
            os.write(bChar);
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
