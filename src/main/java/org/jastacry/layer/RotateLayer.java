package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;

/**
 * Rotate every byte by an offset (either positiv or negativ).
 *
 * <p>SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class RotateLayer extends AbstractBasicLayer
{

    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Rotate Layer";

    /**
     * private range to rate.
     */
    private int offset;

    /**
     * Constructor of XorLayer.
     */
    public RotateLayer()
    {
        super(RotateLayer.class, LAYERNAME);
    }

    /**
     * init function.
     *
     * @param data to initialize the offset value.
     */
    @Override
    public final void init(final String data)
    {
        this.offset = Integer.parseInt(data);
    }

    /**
     * encode Stream function.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            while ((iChar = inputStream.read()) != -1)
            { // NOPMD by kai on 21.11.17 17:19
                iChar += this.offset;
                iChar = rangeCheck(iChar);
                outputStream.write(iChar);
            }
            logger.info("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("encStream failed").initCause(e);
        }
    }

    /**
     * decode Stream function.
     *
     * @param inputStream incoming data
     * @param outputStream outgoging data
     * @throws JastacryException thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            while ((iChar = inputStream.read()) != -1)
            { // NOPMD by kai on 21.11.17 17:19
                iChar -= this.offset;
                iChar = rangeCheck(iChar);
                outputStream.write(iChar);
            }
            logger.info("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("decStream failed").initCause(e);
        }
    }

    @Override
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        throw new UnsupportedOperationException();
    }

}
