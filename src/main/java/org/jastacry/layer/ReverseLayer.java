package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Reverse every bits per byte. 0101 gives 1010 i.e.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class ReverseLayer extends AbstractLayer {
    /**
     * Number of bits to shift back to byte form.
     */
    private static final int SHIFTBITS = 24;

    /**
     * Bit mask to remove anything above one byte.
     */
    private static final int MASKBITS = 0x00000FF;

    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Reverse Layer";

    /**
     * Constructor of XorLayer.
     */
    public ReverseLayer() {
        super(ReverseLayer.class);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize the offset value.
     */
    @Override
    public final void init(final String data) {
        // not used
    }

    /**
     * Local function for encode and decode.
     *
     * @param inputStream
     *            input data stream
     * @param outputStream
     *            output data stream
     * @throws IOException
     *             in case of error
     */
    private void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        while ((iChar = inputStream.read()) != -1) {
            iChar = rangeCheck(iChar);
            iChar = Integer.reverse(iChar);
            iChar >>= SHIFTBITS;
            iChar &= MASKBITS;
            outputStream.write(iChar);
        }
        logger.info("close pipe");
        outputStream.close();
    }

    /**
     * encode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        encodeAndDecode(inputStream, outputStream);
    }

    /**
     * decode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoging data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        encodeAndDecode(inputStream, outputStream);
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

}
