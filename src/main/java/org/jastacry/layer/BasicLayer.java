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
public abstract class BasicLayer implements Runnable {
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

    /**
     * Countdown for managing threads running.
     */
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
        // empty by design, was throwing new UnsupportedOperationException
    }

    /**
     * Local encode Stream function which does the real thing for Random Layer.
     *
     * @param newInputStream
     *            incoming data
     * @param newOutputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    protected abstract void encodeAndDecode(final InputStream newInputStream, final OutputStream newOutputStream) throws IOException;

    /**
     * Encodes either plain text or an encoded layer to the next encoding layer.
     *
     * @param newInputStream
     *            existing and opened input stream
     * @param newOutputStream
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public void encStream(final InputStream newInputStream, final OutputStream newOutputStream) throws IOException {
        encodeAndDecode(newInputStream, newOutputStream);
    }

    /**
     * Decodes an encrypted stream to either plain text or the next encoded layer.
     *
     * @param newInputStream
     *            existing and opened input stream
     * @param newOutputStream
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public void decStream(final InputStream newInputStream, final OutputStream newOutputStream) throws IOException {
        encodeAndDecode(newInputStream, newOutputStream);
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

    /**
     * Property setter for input stream.
     * @param newInputStream the new stream
     */
    public final void setInputStream(final InputStream newInputStream) {
        this.inputStream = newInputStream;
    }

    /**
     * property setter for output stream.
     * @param newOutputStream the new output stream
     */
    public final void setOutputStream(final OutputStream newOutputStream) {
        this.outputStream = newOutputStream;
    }

    /**
     * Property setter for action.
     * @param newAction the new action
     */
    public final void setAction(final Action newAction) {
        this.action = newAction;
    }

    /**
     * Property setter for endcontroller.
     * @param newEndController the new endcontroller
     */
    public final void setEndController(final CountDownLatch newEndController) {
        this.endController = newEndController;
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
