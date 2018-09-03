package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData.Action;
import org.jastacry.JastacryException;

/**
 * Abstract base class for the actual worker layers.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public abstract class AbstractBasicLayer implements Runnable, Layer
{
    /**
     * When a byte is too little.
     */
    private static final int BYTE_VALUE_OVER = 256;

    /**
     * Maximum value for a byte value.
     */
    private static final int BYTE_VALUE_MAX = 255;

    /**
     * Name of the childs layer implementation.
     */
    private String realLayerName;

    /**
     * Action for encoding or decoding direction.
     */
    private Action action;

    /**
     * Input stream.
     */
    protected InputStream inputStream;

    /**
     * Output stream.
     */
    protected OutputStream outputStream;

    /**
     * Logger object.
     */
    protected final Logger logger;

    /**
     * Countdown for managing threads running.
     */
    protected CountDownLatch endController;

    /**
     * Constructor of Layer.
     *
     * @param caller class object
     * @param layerName name of real layer
     */
    protected AbstractBasicLayer(final Class<?> caller, final String layerName)
    {
        logger = LogManager.getLogger(caller);
        setAction(null);
        setInputStream(null);
        setOutputStream(null);
        setRealLayerName(layerName);
    }


    /**
     * Show a human readable name of the layer.
     *
     * @return a human readable name of the layer
     * @see java.lang.Object#toString()
     */
    public final String toString()
    {
        return realLayerName;
    }

    /**
     * Private range check function for byte values.
     *
     * @param iInput as input value
     * @return range checked byte value
     */
    protected final int rangeCheck(final int iInput)
    {
        int iTmp = iInput;
        if (iTmp < 0)
        {
            iTmp += BYTE_VALUE_OVER;
        }
        else
        {
            if (iTmp > BYTE_VALUE_MAX)
            {
                iTmp -= BYTE_VALUE_OVER;
            }
        }

        return iTmp;
    }

    /* (non-Javadoc)
     * @see org.jastacry.layer.Layer#setInputStream(java.io.InputStream)
     */
    @Override
    public final void setInputStream(final InputStream newInputStream)
    {
        this.inputStream = newInputStream;
    }

    /* (non-Javadoc)
     * @see org.jastacry.layer.Layer#setOutputStream(java.io.OutputStream)
     */
    @Override
    public final void setOutputStream(final OutputStream newOutputStream)
    {
        this.outputStream = newOutputStream;
    }

    /* (non-Javadoc)
     * @see org.jastacry.layer.Layer#setAction(org.jastacry.GlobalData.Action)
     */
    @Override
    public final void setAction(final Action newAction)
    {
        this.action = newAction;
    }

    /**
     * Property setter for endcontroller.
     *
     * @param newEndController the new endcontroller
     */
    public final void setEndController(final CountDownLatch newEndController)
    {
        this.endController = newEndController;
    }

    /* (non-Javadoc)
     * @see org.jastacry.layer.Layer#setRealLayerName(java.lang.String)
     */
    @Override
    public final void setRealLayerName(final String newRealLayerName)
    {
        this.realLayerName = newRealLayerName;
    }

    /**
     * Thread entry function for layer work.
     */
    @Override
    public void run()
    {
        logger.info("started thread");
        try
        {
            switch (action)
            {
                case ENCODE:
                    this.encStream(inputStream, outputStream);
                    break;
                case DECODE:
                    this.decStream(inputStream, outputStream);
                    break;
                case UNKOWN:
                default:
                    logger.error("unknown action '{}'", action);
                    break;
            }
            outputStream.close();
        }
        catch (final JastacryException | IOException exception)
        {
            logger.catching(exception);
        }
        finally
        {
            endController.countDown();
        }
        logger.info("finished thread");
    }
}
