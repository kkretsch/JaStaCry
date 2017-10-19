package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Rotate every byte by an offset (either positiv or negativ).
 * 
 * @author Kai Kretschmann
 * @version 0.1.20130818
 */

public class RotateLayer extends AbsLayer {
    public final static String LAYERNAME = "Rotate Layer";
    private int iOffset;

    @Override
    public void init(final String data) {
        this.iOffset = Integer.parseInt(data);
    }

    @Override
    public void encStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        while ((iChar = is.read()) != -1) {
            iChar += this.iOffset;
            rangeCheck(iChar);
            os.write(iChar);
        }
    }

    @Override
    public void decStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        while ((iChar = is.read()) != -1) {
            iChar -= this.iOffset;
            rangeCheck(iChar);
            os.write(iChar);
        }
    }

    @Override
    public String toString() {
        return LAYERNAME;
    }

    private int rangeCheck(final int i) {
        int iTmp = i;
        if (iTmp < 0) {
            iTmp += 256;
        } else {
            if (iTmp > 255) {
                iTmp -= 256;
            }
        }

        return iTmp;
    }

}
