package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;

/**
 * @author Kai Kretschmann
 */

public class EncodeDecodeLayer extends AbstractLayer {
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Encode Layer";

    /**
     * Constructor of EncodeDecodeLayer.
     */
    public EncodeDecodeLayer() {
        super(EncodeDecodeLayer.class);
    }

    /**
     * init function.
     *
     * @param data
     *            to initialize nothing.
     */
    @Override
    public final void init(final String data) {
        // Empty at the moment
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
        OutputStream osEncoded = null; // NOPMD by kai on 21.11.17 17:26
        try {
            osEncoded = javax.mail.internet.MimeUtility.encode(outputStream, "uuencode");
        } catch (final MessagingException e) {
            logger.catching(e);
        }
        final int iCount = IOUtils.copy(inputStream, osEncoded);
        logger.debug("copied {} bytes", iCount);
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
        InputStream isDecoded = null; // NOPMD by kai on 21.11.17 17:26
        try {
            isDecoded = javax.mail.internet.MimeUtility.decode(inputStream, "uuencode"); // NOPMD by kai on 21.11.17 17:26
        } catch (final MessagingException e) {
            logger.catching(e);
            throw new IOException(e.getLocalizedMessage()); // NOPMD by kai on 21.11.17 17:26
        }
        IOUtils.copy(isDecoded, outputStream);
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
