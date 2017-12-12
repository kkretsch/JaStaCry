package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Rotate every byte by an offset (either positiv or negativ).
 *
 * @author Kai Kretschmann
 */

public class RotateLayer extends AbstractLayer {
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

    /**
     * init function.
     *
     * @param data
     *            to initialize the offset value.
     */
    @Override
    public final void init(final String data) {
        this.iOffset = Integer.parseInt(data);
    }

    /**
     * encode Stream function.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:19
            iChar += this.iOffset;
            iChar = rangeCheck(iChar);
            outputStream.write(iChar);
        }
        logger.info("close pipe");
        outputStream.close();
    }

    /**
     * decode Stream function.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:19
            iChar -= this.iOffset;
            iChar = rangeCheck(iChar);
            outputStream.write(iChar);
        }
        logger.info("close pipe");
        outputStream.close();
    }

    /**
     * Print layer name function.
     *
     * @return Layer name as String
     */
    @Override
    public final String toString() {
        return LAYERNAME;
    }

    /**
     * Private range check function for byte values.
     *
     * @param iInput
     *            as input value
     * @return range checked byte value
     */
    private int rangeCheck(final int iInput) {
        int iTmp = iInput;
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
