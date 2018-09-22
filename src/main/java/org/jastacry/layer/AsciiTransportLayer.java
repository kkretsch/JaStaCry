package org.jastacry.layer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.jastacry.JastacryException;

/**
 * Helper class for encode decode.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class AsciiTransportLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "ASCII Layer";

    /**
     * Constructor of EncodeDecodeLayer.
     */
    public AsciiTransportLayer()
    {
        super(AsciiTransportLayer.class, LAYERNAME);
    }

    /**
     * init function.
     *
     * @param data to initialise nothing.
     */
    @Override
    public final void init(final String data)
    {
        // Empty at the moment
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
            final Base64.Encoder encoder = Base64.getEncoder();
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            final byte[] bytesEncoded = encoder.encode(bytes);
            final ByteArrayInputStream inputByteStream = new ByteArrayInputStream(bytesEncoded);
            final int iCount = IOUtils.copy(inputByteStream, outputStream);
            logger.debug("encStream copied {} bytes", iCount);
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
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            final Base64.Decoder decoder = Base64.getDecoder();
            final InputStream isDecoded = decoder.wrap(inputStream);
            final int iCount = IOUtils.copy(isDecoded, outputStream);
            logger.debug("decStream copied {} bytes", iCount);
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("encStream failed").initCause(e);
        }
    }

    @Override
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof AsciiTransportLayer))
        {
            return false;
        }

        return true;
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
