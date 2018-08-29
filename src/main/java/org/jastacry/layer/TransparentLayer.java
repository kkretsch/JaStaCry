package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;

/**
 * A transparent layer just doing nothing with the data. Use it as an example framework to start with.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class TransparentLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Transparent Layer";

    /**
     * Constructor of TransparentLayer.
     */
    public TransparentLayer()
    {
        super(TransparentLayer.class, LAYERNAME);
    }

    /**
     * encode Stream function which does the real thing.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            while ((iChar = inputStream.read()) != -1)
            { // NOPMD by kai on 21.11.17 17:13
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
     * init function.
     *
     * @param data to initialise nothing.
     */
    @Override
    public final void init(final String data)
    {
        // empty by intend
    }

}
