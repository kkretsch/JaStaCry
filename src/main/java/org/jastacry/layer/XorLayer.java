package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Very simple algorithm just to infuse some more complex data rotation.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class XorLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Xor Layer";
    /**
     * private byte mask to xor with.
     */
    private byte bitMask;

    /**
     * Constructor of XorLayer.
     */
    public XorLayer()
    {
        super(XorLayer.class, LAYERNAME);
    }

    /**
     * init function.
     *
     * @param data to initialize the xor value.
     */
    @Override
    public final void init(final String data)
    {
        this.bitMask = (byte) Integer.parseInt(data);
    }

    /**
     * Local encode Stream function which does the real thing for Xor Layer.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            byte bChar;
            while ((iChar = inputStream.read()) != -1)
            { // NOPMD by kai on 21.11.17 17:18
                bChar = (byte) iChar; // NOPMD by kai on 21.11.17 17:18
                bChar = (byte) (bChar ^ this.bitMask);
                outputStream.write(bChar);
                progress(1);
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
        return o == this || o instanceof XorLayer && ((XorLayer) o).bitMask == this.bitMask;
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(bitMask);
    }
}
