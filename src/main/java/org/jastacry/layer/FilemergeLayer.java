package org.jastacry.layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Mask every byte with data of a given file. If the file is smaller than the
 * data it will be used again and again from the beginning.
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
     * @param data
     *            to initialize the file.
     */
    @Override
    public final void init(final String data)
    {
        this.fileMerge = new File(data);
    }

    /**
     * merge Stream function.
     *
     * @param inputStream
     *            input stream
     * @param outputStream
     *            output stream
     * @throws IOException
     *             in case of error
     */
    protected final void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream)
            throws IOException
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
    }

}
