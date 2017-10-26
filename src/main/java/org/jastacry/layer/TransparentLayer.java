package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A transparent layer just doing nothing with the data. Use it as an example
 * framework to start with.
 *
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class TransparentLayer extends AbsLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Transparent Layer";

    /**
     * Constructor of TransparentLayer.
     */
    public TransparentLayer() {
        super(TransparentLayer.class);
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
        while ((iChar = is.read()) != -1) {
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
    public final void decStream(final InputStream is, final OutputStream os)
            throws IOException {
        int iChar;
        while ((iChar = is.read()) != -1) {
            os.write(iChar);
        }
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize nothing.
     */
    public final void init(final String data) {
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
