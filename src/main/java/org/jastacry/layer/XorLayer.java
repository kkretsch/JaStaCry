package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Very simple algorithm just to infuse some more complex data rotation.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class XorLayer extends AbstractBasicLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Xor Layer";
    /**
     * private byte mask to xor with.
     */
    private byte bMask; // NOPMD by kkretsch on 29.03.18 14:53

    /**
     * Constructor of XorLayer.
     */
    public XorLayer() {
        super(XorLayer.class, LAYERNAME);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize the xor value.
     */
    @Override
    public final void init(final String data) {
        this.bMask = (byte) Integer.parseInt(data);
    }

    /**
     * Local encode Stream function which does the real thing for Xor Layer.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int iChar;
        byte bChar;
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:18
            bChar = (byte) iChar; // NOPMD by kai on 21.11.17 17:18
            bChar = (byte) (bChar ^ this.bMask);
            outputStream.write(bChar);
        }
        logger.info("close pipe");
        outputStream.close();
    }

}
