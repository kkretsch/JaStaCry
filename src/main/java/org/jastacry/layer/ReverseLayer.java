package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Reverse every bits per byte. 0101 gives 1010 i.e.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class ReverseLayer extends AbstractBasicLayer
{
    /**
     * Number of bits to shift back to byte form.
     */
    private static final int SHIFTBITS = 24;

    /**
     * Bit mask to remove anything above one byte.
     */
    private static final int MASKBITS = 0x00000FF;

    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Reverse Layer";

    /**
     * Constructor of XorLayer.
     */
    public ReverseLayer()
    {
        super(ReverseLayer.class, LAYERNAME);
    }

    /**
     * init function.
     *
     * @param data to initialize the offset value.
     */
    @Override
    public final void init(final String data)
    {
        // not used
    }

    /**
     * Local encode Stream function which does the real thing for Reverse Layer.
     *
     * @param inputStream input data stream
     * @param outputStream output data stream
     * @throws JastacryException in case of error
     */
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            while ((iChar = inputStream.read()) != -1)
            {
                iChar = rangeCheck(iChar);
                iChar = Integer.reverse(iChar);
                iChar >>= SHIFTBITS;
                iChar &= MASKBITS;
                outputStream.write(iChar);
            }
            logger.info("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("encodeAndDecode failed").initCause(e);
        }
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    @Override
    public boolean equals(final Object o)
    {
        return o == this || o instanceof ReverseLayer;
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
