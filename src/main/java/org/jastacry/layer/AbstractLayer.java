package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for the actual worker layers.
 *
 * @author Kai Kretschmann
 */

public abstract class AbstractLayer {
    /**
     * When a byte is too little.
     */
    private static final int BYTE_VALUE_OVER = 256;

    /**
     * Maximum value for a byte value.
     */
    private static final int BYTE_VALUE_MAX = 255;

    /**
     * Logger object.
     */
    protected Logger logger; // NOPMD by kai on 21.11.17 17:33

    /**
     * Constructor of Layer.
     *
     * @param caller
     *            class object
     */
    protected AbstractLayer(final Class<?> caller) {
        logger = LogManager.getLogger(caller);
    }

    /**
     * Optional method for setting encryption or decryption parameters like keys or passwords.
     *
     * @param data
     *            a String containing everything the layer needs to know
     */
    public abstract void init(String data);

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
    public abstract void encStream(InputStream inputStream, OutputStream outputStream) throws IOException;

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
    public abstract void decStream(InputStream inputStream, OutputStream outputStream) throws IOException;

    /**
     * Show a human readable name of the layer.
     *
     * @return a human readable name of the layer
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();

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
}
