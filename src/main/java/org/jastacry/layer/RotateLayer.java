package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Rotate every byte by an offset (either positiv or negativ).
 *
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class RotateLayer extends AbsLayer {
    /**
     * When a byte is too little.
     */
    private static final int BYTE_VALUE_OVER = 256;

    /**
     * Maximum value for a byte value.
     */
    private static final int BYTE_VALUE_MAX = 255;

    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Rotate Layer";

    /**
     * private range to rate.
     */
    private int iOffset;

    /**
     * Constructor of XorLayer.
     */
    public RotateLayer() {
        super(RotateLayer.class);
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize the offset value.
     */
    public final void init(final String data) {
        this.iOffset = Integer.parseInt(data);
    }

    @Override
    /**
     * encode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void encStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        while ((iChar = is.read()) != -1) {
            iChar += this.iOffset;
            iChar = rangeCheck(iChar);
            os.write(iChar);
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
    public final void decStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        while ((iChar = is.read()) != -1) {
            iChar -= this.iOffset;
            iChar = rangeCheck(iChar);
            os.write(iChar);
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

    /**
     * Private range check function for byte values.
     *
     * @param i
     *            as input value
     * @return range checked byte value
     */
    private int rangeCheck(final int i) {
        int iTmp = i;
        if (iTmp < 0) {
            iTmp += BYTE_VALUE_OVER;
        } else {
            if (iTmp > BYTE_VALUE_MAX) {
                iTmp -= BYTE_VALUE_OVER;
            }
        }

        return iTmp;
    }

}
