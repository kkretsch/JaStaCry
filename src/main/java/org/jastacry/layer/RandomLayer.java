package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mask every byte with some random data.
 * The random stream is initialized by the init seed. Must be used the same on
 * both sides (encryption and decryption).
 *
 * @author Kai Kretschmann
 */

public class RandomLayer extends AbstractLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Random Layer";

    /**
     * Random number generator.
     */
    private final transient java.util.Random rand = new java.util.Random();

    /**
     * Constructor of XorLayer.
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
     * encode Stream function.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        byte bChar;
        final byte[] bRand = new byte[1]; // NOPMD by kai on 21.11.17 17:22
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:22
            bChar = (byte) iChar; // NOPMD by kai on 21.11.17 17:21
            this.rand.nextBytes(bRand);
            bChar = (byte) (bChar ^ bRand[0]);
            outputStream.write(bChar);
        }
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
        byte bChar;
        final byte[] bRand = new byte[1]; // NOPMD by kai on 21.11.17 17:22
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:22
            bChar = (byte) iChar; // NOPMD by kai on 21.11.17 17:22
            this.rand.nextBytes(bRand);
            bChar = (byte) (bChar ^ bRand[0]);
            outputStream.write(bChar);
        }
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
