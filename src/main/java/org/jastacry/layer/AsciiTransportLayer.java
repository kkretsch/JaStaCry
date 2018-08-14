package org.jastacry.layer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

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
     * @param data
     *            to initialize nothing.
     */
    @Override
    public final void init(final String data)
    {
        // Empty at the moment
    }

    /**
     * encode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void encStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        final Base64.Encoder encoder = Base64.getEncoder();
        final byte[] bytes = IOUtils.toByteArray(inputStream);
        final byte[] bytesEncoded = encoder.encode(bytes);
        final ByteArrayInputStream in = new ByteArrayInputStream(bytesEncoded);
        final int iCount = IOUtils.copy(in, outputStream);
        logger.debug("encStream copied {} bytes", iCount);
    }

    /**
     * decode Stream function.
     *
     * @param inputStream
     *            incoming data
     * @param outputStream
     *            outgoing data
     * @throws IOException
     *             thrown on error
     */
    @Override
    public final void decStream(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        final Base64.Decoder decoder = Base64.getDecoder();
        final InputStream isDecoded = decoder.wrap(inputStream);
        final int iCount = IOUtils.copy(isDecoded, outputStream);
        logger.debug("decStream copied {} bytes", iCount);
    }

    @Override
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }

}
