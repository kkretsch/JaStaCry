package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Read write layer for IO purpose.
 *
 * <p>SPDX-License-Identifier: MIT
 * @author kkretsch
 *
 */
public class ReadWriteLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    static final String LAYERNAME = "ReadWrite Layer";

    /**
     * Constructor of class, calling super.
     */
    public ReadWriteLayer()
    {
        super(ReadWriteLayer.class, LAYERNAME);
    }

    @Override
    @SuppressWarnings("ucd")
    public final void run()
    {
        logger.info("started thread");
        try
        {
            int i;
            while ((i = inputStream.read()) != -1)
            {
                outputStream.write(i);
            }
            logger.info("close stream");
            outputStream.close();
        }
        catch (final IOException e)
        {
            logger.catching(e);
        }
        finally
        {
            endController.countDown();
        }
        logger.info("finished thread");
    }

    @Override
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * init function.
     *
     * @param data to initialize nothing.
     */
    @Override
    public final void init(final String data)
    {
        // empty by intend
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    public boolean equals(final Object o)
    {
        return o == this || o instanceof ReadWriteLayer;
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Objects.hash();
    }
}
