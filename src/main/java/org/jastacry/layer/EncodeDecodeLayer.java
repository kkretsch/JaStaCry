package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EncodeDecodeLayer extends AbsLayer {
    public final static String LAYERNAME = "Encode Layer";

    @Override
    public void init(final String data) {
    }

    @SuppressWarnings("restriction")
    @Override
    public void encStream(final InputStream is, final OutputStream os) throws IOException {
        final sun.misc.UUEncoder uuenc = new sun.misc.UUEncoder();
        uuenc.encode(is, os);
    }

    @SuppressWarnings("restriction")
    @Override
    public void decStream(final InputStream is, final OutputStream os) throws IOException {
        final sun.misc.UUDecoder uuenc = new sun.misc.UUDecoder();
        uuenc.decodeBuffer(is, os);
    }

    @Override
    public String toString() {
        return LAYERNAME;
    }

}
