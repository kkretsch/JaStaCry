package org.jastacry.layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Mask every byte with data of a given file. If the file is smaller than the data it will be used again and again from the
 * beginning.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class FilemergeLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Filemerge Layer";

    /**
     * File to merge with..
     */
    private File fileMerge;

    /**
     * Constructor of FilemergeLayer.
     */
    public FilemergeLayer()
    {
        super(FilemergeLayer.class, LAYERNAME);
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
        this.fileMerge = new File(data);
    }

    /**
     * merge Stream function.
     *
     * @param inputStream input stream
     * @param outputStream output stream
     * @throws JastacryException in case of error
     */
    public final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        int iChar;
        int iMerge;
        byte bChar;
        byte bMerge;
        FileChannel channel;

        try (FileInputStream fIS = new FileInputStream(fileMerge))
        {
            channel = fIS.getChannel(); // NOPMD by kai on 21.11.17 17:24

            while ((iChar = inputStream.read()) != -1)
            { // NOPMD by kai on 21.11.17 17:24
                iMerge = fIS.read();

                if (-1 == iMerge)
                {
                    logger.debug("EOF reached, reset to start");
                    channel.position(0);
                    iMerge = fIS.read();
                }
                bChar = (byte) iChar; // NOPMD by kai on 21.11.17 17:24
                bMerge = (byte) iMerge;
                bChar = (byte) (bChar ^ bMerge);
                outputStream.write(bChar);
            }
            logger.info("close pipe");
            outputStream.close();
        } // try with resources
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
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof FilemergeLayer))
        {
            return false;
        }

        final FilemergeLayer layer = (FilemergeLayer) o;
        return layer.fileMerge.equals(this.fileMerge);
    }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(fileMerge);
    }
}
