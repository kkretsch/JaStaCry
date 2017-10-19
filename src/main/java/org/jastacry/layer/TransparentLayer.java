package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A transparent layer just doing nothing with the data.
 * Use it as an example framework to start with.
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class TransparentLayer extends AbsLayer {
	public final static String LAYERNAME="Transparent Layer";

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		while((iChar = is.read()) != -1) {
			os.write(iChar);
		}
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		while((iChar = is.read()) != -1) {
			os.write(iChar);
		}
	}

	@Override
	public void init(String data) {
	}

	@Override
	public String toString() {
		return LAYERNAME;
	}

}
