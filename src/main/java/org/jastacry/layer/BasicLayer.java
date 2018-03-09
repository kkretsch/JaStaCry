package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData.Action;

/**
 * Abstract base class for the actual worker layers.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class BasicLayer implements Runnable {
    /**
     * When a byte is too little.
     */
    private static final int BYTE_VALUE_OVER = 256;

    /**
     * Maximum value for a byte value.
     */
    private static final int BYTE_VALUE_MAX = 255;

    /**
     * Action for encoding or decoding direction.
     */
    private Action action;

    /**
     * Input stream.
     */
    protected InputStream inputStream;

    /**
     * Output stream.
     */
    protected OutputStream outputStream;

    /**
     * Logger object.
     */
    protected Logger logger;

    protected CountDownLatch endController;

    /**
     * Constructor of Layer.
     *
     * @param caller
     *            class object
     */
    protected BasicLayer(final Class<?> caller) {
        logger = LogManager.getLogger(caller);
        setAction(null);
        setInputStream(null);
        setOutputStream(null);
    }

    /**
     * Optional method for setting encryption or decryption parameters like keys or passwords.
     *
     * @param data
     *            a String containing everything the layer needs to know
     */
    public void init(final String data) {
        throw new UnsupportedOperationException();
    }

    /**
     * Encodes either plain text or an encoded layer to the next encoding layer.
     *
     * @param inputStream
     *            existing and opened input stream
     * @param outputStream
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Decodes an encrypted stream to either plain text or the next encoded layer.
     *
     * @param inputStream
     *            existing and opened input stream
     * @param outputStream
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Show a human readable name of the layer.
     *
     * @return a human readable name of the layer
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Private range check function for byte values.
     *
     * @param iInput
     *            as input value
     * @return range checked byte value
     */
    protected final int rangeCheck(final int iInput) {
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

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setAction(final Action action) {
        this.action = action;
    }

    public void setEndController(final CountDownLatch endController) {
        this.endController = endController;
    }

    @Override
    public void run() {
        logger.info("started thread");
        try {
            switch (action) {
                case ENCODE:
                    this.encStream(inputStream, outputStream);
                    break;
                case DECODE:
                    this.decStream(inputStream, outputStream);
                    break;
                case UNKOWN:
                default:
                    logger.error("unknown action '{}'", action);
                    break;
            }
            outputStream.close();
        } catch (final IOException exception) {
            logger.catching(exception);
        } finally {
            endController.countDown();
        }
        logger.info("finished thread");
    }
}
