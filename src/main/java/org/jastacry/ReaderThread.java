package org.jastacry;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

/**
 * Thread class for running layers.
 *
 * @author kkre
 *
 */
public class ReaderThread extends AbstractThread {

    /**
     * Constructor of reader thread.
     *
     * @param oInputStream
     *            incoming data
     * @param oOutputStream
     *            outgoing data
     */
    public ReaderThread(final InputStream oInputStream, final PipedOutputStream oOutputStream) {
        super();

        this.inputStream = oInputStream;
        this.outputStream = oOutputStream;
        setName("Reader");
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
            LOGGER.info("close pipe");
            outputStream.close();
        } catch (final IOException e) {
            LOGGER.catching(e);
        }
        LOGGER.info("finished thread");
    }

}
