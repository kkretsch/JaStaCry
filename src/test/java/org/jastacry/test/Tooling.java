package org.jastacry.test;

import java.io.File;
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
}
