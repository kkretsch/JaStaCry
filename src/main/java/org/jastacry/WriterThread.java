package org.jastacry;

import java.io.OutputStream;
import java.io.PipedInputStream;

/**
 * Thread class for running layers.
 *
 * @author kkre
 *
 */
public class WriterThread extends ReadWriteThread {

    /**
     * Constructor of writer thread.
     *
     * @param oInputStream
     *            piped incoming data
     * @param oOutputStream
     *            outgoing data
     */
    public WriterThread(final PipedInputStream oInputStream, final OutputStream oOutputStream) {
        super();

        this.inputStream = oInputStream;
        this.outputStream = oOutputStream;
        setName("Writer");
    }

}
