package org.jastacry.layer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mask every byte with data of a given file.
 * If the file is smaller than the data it will be used again and again from the beginning.
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class FilemergeLayer extends AbsLayer {
	File fileMerge;
	InputStream merge = null;

	@Override
	public void init(String data) {
		this.fileMerge = new File(data);
	}

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		int iMerge;
		byte bChar;
		byte bMerge;

		merge = new BufferedInputStream(new FileInputStream(fileMerge));
		merge.mark(0);

		while((iChar = is.read()) != -1) {
			if(-1 == (iMerge = merge.read())) {
				merge.reset();
				iMerge = merge.read();
			}
			bChar = (byte)iChar;
			bMerge = (byte)iMerge;
			bChar = (byte)(bChar ^ bMerge);
			os.write(bChar);
		}
		
		merge.close();
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
		int iChar;
		int iMerge;
		byte bChar;
		byte bMerge;

		merge = new BufferedInputStream(new FileInputStream(fileMerge));
		merge.mark(0);

		while((iChar = is.read()) != -1) {
			if(-1 == (iMerge = merge.read())) {
				merge.reset();
				iMerge = merge.read();
			}
			bChar = (byte)iChar;
			bMerge = (byte)iMerge;
			bChar = (byte)(bChar ^ bMerge);
			os.write(bChar);
		}
		
		merge.close();
	}

	@Override
	public String toString() {
		return "Filemerge Layer";
	}

}
