package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mask every byte with some random data. The random stream is initialized by the init seed. Must be used the same on
 * both sides (encryption and decryption).
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class RandomLayer extends BasicLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Random Layer";

    /**
     * Random number generator.
     */
    private final java.util.Random rand = new java.util.Random();

    /**
     * Constructor of RandomLayer.
     */
    public RandomLayer() {
        super(RandomLayer.class);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize the random seed value.
     */
    @Override
    public final void init(final String data) {
        final long sSeed = Long.parseLong(data);
        rand.setSeed(sSeed);
    }

    /**
     * Local encode Stream function which does the real thing for Random Layer.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    private void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        byte bChar;
        final byte[] bRand = new byte[1];
        while ((iChar = inputStream.read()) != -1) {
            bChar = (byte) iChar;
            this.rand.nextBytes(bRand);
            bChar = (byte) (bChar ^ bRand[0]);
            outputStream.write(bChar);
        }
        logger.info("close pipe");
        outputStream.close();
    }

    /**
     * encode Stream function for Random Layer.
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
     * decode Stream function for Random Layer.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        encodeAndDecode(inputStream, outputStream);
    }

    /**
     * Print layer name function for Random Layer.
     *
     * @return Layer name as String
     */
    @Override
    public final String toString() {
        return LAYERNAME;
    }

}
