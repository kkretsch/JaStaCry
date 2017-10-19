package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EncodeDecodeLayer  extends AbsLayer {
	public final static String LAYERNAME="Encode Layer";

	@Override
	public void init(String data) {
	}

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
		sun.misc.UUEncoder uuenc = new sun.misc.UUEncoder();
		uuenc.encode(is, os);
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
		sun.misc.UUDecoder uuenc = new sun.misc.UUDecoder();
		uuenc.decodeBuffer(is, os);
	}

	@Override
	public String toString() {
		return LAYERNAME;
	}

}
