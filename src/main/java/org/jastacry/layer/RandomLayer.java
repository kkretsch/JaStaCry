package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mask every byte with some random data. The random stream is initialized by
 * the init seed. Must be used the same on
 * both sides (encription and description).
 *
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class RandomLayer extends AbsLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Random Layer";

    /**
     * Random number generator.
     */
    private java.util.Random rand = new java.util.Random();

    /**
     * Constructor of XorLayer.
     */
    public RandomLayer() {
        super(RandomLayer.class);
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize the random seed value.
     */
    public final void init(final String data) {
        final long s = Long.parseLong(data);
        rand.setSeed(s);
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
        final byte[] bRand = new byte[1];
        while ((iChar = is.read()) != -1) {
            bChar = (byte) iChar;
            this.rand.nextBytes(bRand);
            bChar = (byte) (bChar ^ bRand[0]);
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
        final byte[] bRand = new byte[1];
        while ((iChar = is.read()) != -1) {
            bChar = (byte) iChar;
            this.rand.nextBytes(bRand);
            bChar = (byte) (bChar ^ bRand[0]);
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
