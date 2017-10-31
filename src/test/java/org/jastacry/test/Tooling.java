package org.jastacry.test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper functions for automated tests.
 * @author kai
 *
 */
public class Tooling {
    /**
     * log4j2 object.
     */
    private static Logger logger = LogManager.getLogger();

    /**
     * How big can a byte be.
     */
    private static final int MAXBYTE = 255;

    /**
     * compare two files if they are equal.
     *
     * @param f1 file one
     * @param f2 file two
     * @return true if equal
     */
    public boolean compareFiles(final File f1, final File f2) {
        boolean b = false;
        try {
            b = FileUtils.contentEquals(f1, f2);
        } catch (IOException e) {
            logger.catching(e);
        }
        return b;
    }

    /**
     * Create a binary input file with all byte values possible.
     *
     * @param f File to create
     */
    public void createBinaryTestfile(final File f) {
        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(f));
            for (int i = 0; i <= MAXBYTE; i++) {
                os.writeByte(i);
            }
            os.close();
        } catch (FileNotFoundException e) {
            logger.catching(e);
        } catch (IOException e) {
            logger.catching(e);
        }
    }
}
