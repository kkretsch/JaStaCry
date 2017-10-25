package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for the actual worker layers
 *
 * TODO Version numbers in files
 *
 * @author Kai Kretschmann
 * @version 0.1.20171022
 */

public abstract class AbsLayer {
    public static String LAYERNAME = null;
    Logger logger = null;

    /**
     * Optional method for setting encryption or decryption parameters like keys or passwords
     *
     * @param data
     *            a String containing everything the layer needs to know
     */
    public abstract void init(String data);

    /**
     * Encodes either plain text or an encoded layer to the next encoding layer
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
     * Decodes an encrypted stream to either plain text or the next encoded layer
     *
     * @param is
     *            existing and opened input stream
     * @param os
     *            existing and opened output stream
     * @throws IOException
     *             if one of the streams fail
     */
    public abstract void decStream(InputStream is, OutputStream os) throws IOException;

    /**
     * Show a human readable name of the layer
     *
     * @return a human readable name of the layer
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}
