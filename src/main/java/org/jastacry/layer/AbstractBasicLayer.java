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
     * Total number of bytes the layer has made progress.
     */
    private long totalStepsize = 0;

    /**
     * Total number of calls for progress.
     */
    private long totalStepcount = 0;

    /**
     * How many calls to progress until we log a line.
     */
    private static final long STEP_LOGGING_FACTOR = 1000;

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
        logger.debug("set action to {}", this.action);
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

    /**
     * Call this function for every step forward in encoding or decoding.
     * @param stepsize number of bytes we processed in this step
     */
    protected final void progress(final long stepsize)
    {
        this.totalStepsize += stepsize;
        this.totalStepcount++;

        if ((totalStepcount - 1) % STEP_LOGGING_FACTOR == 0)
        {
            logger.trace("At byte {} after {} calls", totalStepsize, totalStepcount);
        }
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
    @SuppressWarnings("ucd")
    public void run()
    {
        logger.info("started thread for {}", action);
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

    /**
     * Read all wanted bytes from inputStream.
     * @param is the stream to read from
     * @param aTarget target byte array
     * @param len complete wanted length
     * @return number of bytes read
     * @throws IOException in case of error
     */
    protected final int readAllBytes(final InputStream is, final byte[] aTarget, final int len) throws IOException
    {
        var iSumBytes = 0;
        int iRemainingBytes;

        while (iSumBytes < len)
        {
            iRemainingBytes = len - iSumBytes;
            int iReadBytes = is.read(aTarget, iSumBytes, iRemainingBytes);
            logger.trace("Did read {} bytes, expected up to {}", iReadBytes, iRemainingBytes);
            iSumBytes += iReadBytes;
        }

        return iSumBytes;
    }
}
