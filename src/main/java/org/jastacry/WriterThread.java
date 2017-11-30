package org.jastacry;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;

/**
 * Thread class for running layers.
 *
 * @author kkre
 *
 */
public class WriterThread extends AbstractThread {

    /**
     * Constructor of reader thread.
     *
     * @param oInputStream
     *            incoming data
     * @param oOutputStream
     *            outgoing data
     */
    public WriterThread(final PipedInputStream oInputStream, final OutputStream oOutputStream) {
        super();

        this.inputStream = oInputStream;
        this.outputStream = oOutputStream;
        setName("Writer");
    }

    /**
     * main run method.
     */
    @Override
    public void run() {
        LOGGER.info("started thread");
        try {
            int i = 0;
            while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }
        } catch (final IOException e) {
            LOGGER.catching(e);
        }
        LOGGER.info("finished thread");
    }

}
