package org.jastacry;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.jastacry.GlobalData.Action;
import org.jastacry.layer.AbstractLayer;

/**
 * Thread class for running layers.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class LayerThread extends AbstractThread {
    /**
     * Layer to work with.
     */
    private final AbstractLayer layer;

    /**
     * Action for encoding or decoding direction.
     */
    private final Action action;

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
            final AbstractLayer oLayer, final Action iAction, final String sName) {
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
                case ENCODE:
                    layer.encStream(inputStream, outputStream);
                    break;
                case DECODE:
                    layer.decStream(inputStream, outputStream);
                    break;
                case UNKOWN:
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
