package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Read write layer for IO purpose.
 * @author kkretsch
 *
 */
public class ReadWriteLayer extends BasicLayer {

    /**
     * Constructor of class, calling super.
     */
    public ReadWriteLayer() {
        super(ReadWriteLayer.class);
    }

    @Override
    public final void run() {
        logger.info("started thread");
        try {
            int i;
            while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }
            logger.info("close stream");
            outputStream.close();
        } catch (final IOException e) {
            logger.catching(e);
        } finally {
            endController.countDown();
        }
        logger.info("finished thread");
    }

    @Override
    public final String toString() {
        return "ReadWrite";
    }

    @Override
    protected void encodeAndDecode(InputStream inputStream, OutputStream outputStream) throws IOException {
        // not used
    }
}
