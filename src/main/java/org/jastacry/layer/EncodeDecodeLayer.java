package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;

/**
 * @author Kai Kretschmann
 */

public class EncodeDecodeLayer extends AbsLayer {
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
     * encode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void encStream(final InputStream is, final OutputStream os)
            throws IOException {
        OutputStream osEncoded = null;
        try {
            osEncoded = javax.mail.internet.MimeUtility.encode(os, "uuencode");
        } catch (final MessagingException e) {
            logger.catching(e);
        }
        final int iCount = IOUtils.copy(is, osEncoded);
        logger.debug("copied {} bytes", iCount);
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
        InputStream isDecoded = null;
        try {
            isDecoded = javax.mail.internet.MimeUtility.decode(is, "uuencode");
        } catch (final MessagingException e) {
            logger.catching(e);
            throw new IOException(e.getLocalizedMessage());
        }
        IOUtils.copy(isDecoded, os);
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
