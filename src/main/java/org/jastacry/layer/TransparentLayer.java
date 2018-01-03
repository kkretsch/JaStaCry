package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A transparent layer just doing nothing with the data. Use it as an example framework to start with.
 *
 * @author Kai Kretschmann
 */

@java.lang.SuppressWarnings("common-java:DuplicatedBlocks")
public class TransparentLayer extends AbstractLayer {
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

    /**
     * encode Stream function which does the real thing.
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
        while ((iChar = inputStream.read()) != -1) { // NOPMD by kai on 21.11.17 17:13
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
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        encodeAndDecode(inputStream, outputStream);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize nothing.
     */
    @Override
    public final void init(final String data) {
        // empty by intend
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
