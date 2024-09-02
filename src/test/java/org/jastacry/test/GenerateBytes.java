package org.jastacry.test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generate constant files to compare test runs to.
 *
 * @author Kai Kretschmann
 *
 */
public class GenerateBytes
{

    /**
     * Generate file to compare to.
     * 
     * @param args
     *            command line
     * @throws IOException on IO error
     */
    public static void main(final String[] args) throws IOException
    {
        final byte[] bytes = new byte[256];
        Path p = FileSystems.getDefault().getPath(TestMain.RESOURCES, TestMain.INPUTBYTEFILE);

        for(int b=0; b<256; b++) {
            bytes[b] = (byte) b;
        }
        Files.write(p, bytes);
    }
}
