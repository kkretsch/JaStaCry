package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Very simple algorithm just to infuse some more complex data rotation
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class XorLayer extends AbsLayer {
	public final static String LAYERNAME="Xor Layer";
	private byte bMask;

	@Override
	public void init(String data) {
		this.bMask = (byte)Integer.parseInt(data);
	}

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		byte bChar;
		while((iChar = is.read()) != -1) {
			bChar = (byte)iChar;
			bChar = (byte)(bChar ^ this.bMask);
			os.write(bChar);
		}
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		byte bChar;
		while((iChar = is.read()) != -1) {
			bChar = (byte)iChar;
			bChar = (byte)(bChar ^ this.bMask);
			os.write(bChar);
		}
	}

	@Override
	public String toString() {
		return LAYERNAME;
	}

}
