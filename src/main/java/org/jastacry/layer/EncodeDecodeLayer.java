package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;

public class EncodeDecodeLayer extends AbsLayer {
    public final static String LAYERNAME = "Encode Layer";

    @Override
    public void init(final String data) {
        logger = LogManager.getLogger(EncodeDecodeLayer.class);
    }

    @Override
    public void encStream(final InputStream is, final OutputStream os) throws IOException {
        OutputStream osEncoded = null;
        try {
            osEncoded = javax.mail.internet.MimeUtility.encode(os, "uuencode");
        } catch (final MessagingException e) {
            logger.catching(e);
        }
        IOUtils.copy(is, osEncoded);
    }

    @Override
    public void decStream(final InputStream is, final OutputStream os) throws IOException {
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
    public String toString() {
        return LAYERNAME;
    }

}
