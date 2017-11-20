package org.jastacry.layer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
     * Input stream of file to merge with.
     */
    private InputStream merge;

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

    @Override
    /**
     * encode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void encStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        int iMerge;
        byte bChar;
        byte bMerge;

        final FileInputStream fIS = new FileInputStream(fileMerge);
        merge = new BufferedInputStream(fIS);

        while ((iChar = is.read()) != -1) {
            iMerge = merge.read();
            if (-1 == iMerge) {
                logger.debug("EOF reached, reset to start");
                fIS.getChannel().position(0);
                merge = new BufferedInputStream(fIS);
                iMerge = merge.read();
            }
            bChar = (byte) iChar;
            bMerge = (byte) iMerge;
            bChar = (byte) (bChar ^ bMerge);
            os.write(bChar);
        }

        merge.close();
        fIS.close();
    }

    @Override
    /**
     * decode Stream function.
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public final void decStream(final InputStream is, final OutputStream os) throws IOException {
        int iChar;
        int iMerge;
        byte bChar;
        byte bMerge;

        final FileInputStream fIS = new FileInputStream(fileMerge);
        merge = new BufferedInputStream(fIS);

        while ((iChar = is.read()) != -1) {
            iMerge = merge.read();
            if (-1 == iMerge) {
                logger.debug("EOF reached, reset to start");
                fIS.getChannel().position(0);
                merge = new BufferedInputStream(fIS);
                iMerge = merge.read();
            }
            bChar = (byte) iChar;
            bMerge = (byte) iMerge;
            bChar = (byte) (bChar ^ bMerge);
            os.write(bChar);
        }

        merge.close();
        fIS.close();
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
