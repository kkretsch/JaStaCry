package org.jastacry.layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Append the bytes from a file to the current byte stream.
 * On decryption just strip it from the byte stream.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class AppendLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Append Layer";

    /**
     * File to append after data.
     */
    private File fileAppend;

    /**
     * Constructor of FilemergeLayer.
     */
    public AppendLayer()
    {
        super(AppendLayer.class, LAYERNAME);
        logger.debug("create layer class");
        this.fileAppend = null;
    }

    /**
     * init function.
     *
     * @param data to initialise the file.
     */
    @SuppressWarnings("squid:S4797") // Handling files is security-sensitive
    @Override
    public final void init(final String data)
    {
        logger.info("use file '{}' for appending", data);
        this.fileAppend = new File(data);
    }

    /**
     * Encode data.
     */
    @Override
    public void encStream(InputStream inputStream, OutputStream outputStream) throws JastacryException
    {
        if(null == fileAppend)
        {
            throw (JastacryException) new JastacryException("encodeAndDecode failed, append file undefined");
        }

        try(FileInputStream fIS = new FileInputStream(fileAppend))
        {
            logger.debug("read data input stream");
            int iChar;
            while ((iChar = inputStream.read()) != -1)
            {
                outputStream.write(iChar);
                progress(1);
            }
            logger.debug("read append file stream");
            while ((iChar = fIS.read()) != -1)
            {
                outputStream.write(iChar);
                progress(1);
            }

            logger.debug("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("encodeAndDecode failed").initCause(e);
        }
    }

    /**
     * Decode back.
     */
    @Override
    public void decStream(InputStream inputStream, OutputStream outputStream) throws JastacryException
    {
        if(null == fileAppend)
        {
            throw (JastacryException) new JastacryException("encodeAndDecode failed, append file undefined");
        }
        long lFileSize = fileAppend.length();

        try
        {
            if(0 == inputStream.available())
            {
                throw (JastacryException) new JastacryException("encodeAndDecode failed, input stream empty");
            }

            byte[] buf = inputStream.readAllBytes();
            if(buf.length < lFileSize)
            {
                throw (JastacryException) new JastacryException("decStream failed, too short");
            }
            int lDataSize = (int) (buf.length-lFileSize);
            byte[] bufData = new byte[lDataSize];
            java.lang.System.arraycopy(buf, 0, bufData, 0, lDataSize);
            outputStream.write(bufData);

            logger.debug("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("decStream failed").initCause(e);
        }
    }

    /**
     * merge Stream function.
     *
     * @param inputStream input stream
     * @param outputStream output stream
     * @throws JastacryException in case of error
     */
    @Override
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        throw new RuntimeException("Combined method not to be used here.");
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof AppendLayer))
        {
            return false;
        }

        final AppendLayer layer = (AppendLayer) o;
        return layer.fileAppend.equals(this.fileAppend);
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(fileAppend);
    }
}
