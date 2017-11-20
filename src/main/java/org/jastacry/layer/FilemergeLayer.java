package org.jastacry.layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Mask every byte with data of a given file. If the file is smaller than the data it will be used again and again from
 * the beginning.
 *
 * @author Kai Kretschmann
 */

public class FilemergeLayer extends AbsLayer {
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
    public FilemergeLayer() {
        super(FilemergeLayer.class);
    }

    @Override
    /**
     * init function.
     *
     * @param data
     *            to initialize the file.
     */
    public final void init(final String data) {
        this.fileMerge = new File(data);
    }

    /**
     * merge Stream function.
     *
     * @param is
     *            input stream
     * @param os
     *            output stream
     * @throws IOException
     *             in case of error
     */
    private void mergeStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        int iMerge;
        byte bChar;
        byte bMerge;
        final FileChannel channel;

        try (FileInputStream fIS = new FileInputStream(fileMerge)) {
            channel = fIS.getChannel();

            while ((iChar = is.read()) != -1) {
                iMerge = fIS.read();

                if (-1 == iMerge) {
                    logger.debug("EOF reached, reset to start");
                    channel.position(0);
                    iMerge = fIS.read();
                }
                bChar = (byte) iChar;
                bMerge = (byte) iMerge;
                bChar = (byte) (bChar ^ bMerge);
                os.write(bChar);
            }
        } // try with resources
    }

    @Override
    /**
     * encode Stream function.
     *
     * @param is
     *            input stream
     * @param os
     *            output stream
     * @throws IOException
     *             in case of error
     */
    public final void encStream(final InputStream is, final OutputStream os) throws IOException {
        mergeStream(is, os);
    }

    @Override
    /**
     * decode Stream function.
     *
     * @param is
     *            input stream
     * @param os
     *            output stream
     * @throws IOException
     *             in case of error
     */
    public final void decStream(final InputStream is, final OutputStream os) throws IOException {
        mergeStream(is, os);
    }

    @Override
    /**
     * Print layer name function.
     *
     * @return Layer name as String
     */
    public final String toString() {
        return LAYERNAME;
    }

}
