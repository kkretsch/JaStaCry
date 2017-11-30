package org.jastacry;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.AbstractLayer;

/**
 * Thread class for running layers.
 *
 * @author kkre
 *
 */
public class LayerThread extends Thread {
    /**
     * log4j logger object.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Input stream.
     */
    private final PipedInputStream inputStream;

    /**
     * Output stream.
     */
    private final PipedOutputStream outputStream;

    /**
     * Layer to work with.
     */
    private final transient AbstractLayer layer;

    /**
     * Action for encoding or decoding direction.
     */
    private final transient int action;

    /**
     * Constructor of layers thread.
     *
     * @param oInputStream
     *            incoming data
     * @param oOutputStream
     *            outgoing data
     * @param oLayer
     *            encrypting layer
     * @param iAction
     *            action type
     * @param sName
     *            verbose name
     */
    public LayerThread(final PipedInputStream oInputStream, final PipedOutputStream oOutputStream,
            final AbstractLayer oLayer, final int iAction, final String sName) {
        super();

        this.inputStream = oInputStream;
        this.outputStream = oOutputStream;
        this.layer = oLayer;
        this.action = iAction;
        setName(sName);
    }

    /**
     * main run method.
     */
    @Override
    public void run() {
        LOGGER.info("started thread");
        try {
            switch (action) {
                case org.jastacry.GlobalData.ENCODE:
                    layer.encStream(inputStream, outputStream);
                    break;
                case org.jastacry.GlobalData.DECODE:
                    layer.decStream(inputStream, outputStream);
                    break;
                default:
                    LOGGER.error("unknwon action '{}'", action);
                    break;
            }
        } catch (final IOException exception) {
            LOGGER.catching(exception);
        }
        LOGGER.info("finished thread");
    }

    /**
     * get Stream for next or previous thread.
     * 
     * @return the stream
     */
    public PipedInputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * get Stream for next or previous thread.
     * 
     * @return the stream
     */
    public PipedOutputStream getOutputStream() {
        return this.outputStream;
    }

}
