package org.jastacry;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.jastacry.layer.AbstractLayer;

/**
 * Thread class for running layers.
 *
 * @author kkre
 *
 */
public class LayerThread extends AbstractThread {
    /**
     * Layer to work with.
     */
    private final AbstractLayer layer;

    /**
     * Action for encoding or decoding direction.
     */
    private final int action;

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
    public final void run() {
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
                    LOGGER.error("unknown action '{}'", action);
                    break;
            }
            outputStream.close();
        } catch (final IOException exception) {
            LOGGER.catching(exception);
        }
        LOGGER.info("finished thread");
    }

}
