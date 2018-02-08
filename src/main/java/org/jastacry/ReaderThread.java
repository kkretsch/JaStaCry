package org.jastacry;

import java.io.InputStream;
import java.io.PipedOutputStream;

/**
 * Thread class for running layers.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class ReaderThread extends ReadWriteThread {

    /**
     * Constructor of reader thread.
     *
     * @param oInputStream
     *            incoming data
     * @param oOutputStream
     *            piped outgoing data
     */
    public ReaderThread(final InputStream oInputStream, final PipedOutputStream oOutputStream) {
        super();

        this.inputStream = oInputStream;
        this.outputStream = oOutputStream;
        setName("Reader");
    }

}
