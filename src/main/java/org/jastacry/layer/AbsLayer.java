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

public abstract class AbsLayer {

    /**
     * Logger object.
     */
    protected Logger logger = null;

    /**
     * Constructor of Layer.
     *
     * @param caller
     *            class object
     */
    protected AbsLayer(final Class<?> caller) {
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
     * @param is
     *            existing and opened input stream
     * @param os
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public abstract void encStream(InputStream is, OutputStream os) throws IOException;

    /**
     * Decodes an encrypted stream to either plain text or the next encoded layer.
     *
     * @param is
     *            existing and opened input stream
     * @param os
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public abstract void decStream(InputStream is, OutputStream os) throws IOException;

    @Override
    /**
     * Show a human readable name of the layer
     *
     * @return a human readable name of the layer
     * @see java.lang.Object#toString()
     */
    public abstract String toString();
}
