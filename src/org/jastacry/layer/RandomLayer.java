package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mask every byte with some random data.
 * The random stream is initialized by the init seed. Must be used the same on both sides (encription and description).
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class RandomLayer extends AbsLayer {
	public final static String LAYERNAME="Random Layer";

	java.util.Random rand = new java.util.Random();

	@Override
	public void init(String data) {
		long s = Long.parseLong(data);
		rand.setSeed(s);
	}

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		byte bChar;
		byte bRand[] = new byte[1];
		while((iChar = is.read()) != -1) {
			bChar = (byte)iChar;
			this.rand.nextBytes(bRand);
			bChar = (byte)(bChar ^ bRand[0]);
			os.write(bChar);
		}
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		byte bChar;
		byte bRand[] = new byte[1];
		while((iChar = is.read()) != -1) {
			bChar = (byte)iChar;
			this.rand.nextBytes(bRand);
			bChar = (byte)(bChar ^ bRand[0]);
			os.write(bChar);
		}
	}

	@Override
	public String toString() {
		return LAYERNAME;
	}

}
