package org.jastacry;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Thread class for running layers.
 *
 * @author kkre
 *
 */
public abstract class AbstractThread extends Thread {
    /**
     * log4j logger object.
     */
    protected static final Logger LOGGER = LogManager.getLogger();

    /**
     * Input stream.
     */
    protected InputStream inputStream = null;

    /**
     * Output stream.
     */
    protected OutputStream outputStream = null;

    /**
     * main run method.
     */
    @Override
    public abstract void run();

    /**
     * get Stream for next or previous thread.
     *
     * @return the stream
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * get Stream for next or previous thread.
     *
     * @return the stream
     */
    public OutputStream getOutputStream() {
        return this.outputStream;
    }
}
