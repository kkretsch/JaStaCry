package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;

public class EncodeDecodeLayer extends AbsLayer {
    public final static String LAYERNAME = "Encode Layer";
    private final static String UUNAME = "jastacry";

    @Override
    public void init(final String data) {
    }

    @Override
    public void encStream(final InputStream is, final OutputStream os) throws IOException {
        final org.apache.tools.ant.util.UUEncoder uuenc = new org.apache.tools.ant.util.UUEncoder(UUNAME);
        uuenc.encode(is, os);
    }

    @Override
    public void decStream(final InputStream is, final OutputStream os) throws IOException {
        InputStream isDecoded = null;
        try {
            isDecoded = javax.mail.internet.MimeUtility.decode(is, "uuencode");
        } catch (final MessagingException exception) {
            exception.printStackTrace();
            throw new IOException(exception.getLocalizedMessage());
        }
        IOUtils.copy(isDecoded, os);
    }

    @Override
    public String toString() {
        return LAYERNAME;
    }

}
