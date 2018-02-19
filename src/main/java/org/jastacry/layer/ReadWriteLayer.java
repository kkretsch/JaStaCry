package org.jastacry.layer;

import java.io.IOException;

public class ReadWriteLayer extends BasicLayer {

    public ReadWriteLayer() {
        super(ReadWriteLayer.class);
    }

    @Override
    public void run() {
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
    public String toString() {
        return "ReadWrite";
    }
}
