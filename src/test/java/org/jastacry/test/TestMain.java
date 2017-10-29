/**
 *
 */
package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.JaStaCry;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestMain {
    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * Test configuration file.
     */
    public static final String CONF1 = "conf1.cfg";

    /**
     * Test input text file.
     */
    public static final String INPUTFILE = "plaintext.txt";

    /**
     * Test input encoded file.
     */
    public static final String INPUTENCODED = "encoded.dat";

    /**
     * temporary file.
     */
    private File tmpFile;

    /**
     * The BeforeClass method.
     *
     * @throws MalformedURLException
     *             in case of error.
     */
    @BeforeClass
    public static void setLogger() throws MalformedURLException {
        oLogger = LogManager.getLogger();
    }

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception {
        try {
            tmpFile = File.createTempFile(org.jastacry.Data.TMPBASE, null);
        } catch (final IOException e1) {
            oLogger.catching(e1);
        }
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case of error
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testMainHelp() {
        final String[] sArguments = {"-h"};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, org.jastacry.Data.RC_HELP);
    }

    /**
     * Test method no parameters error for Main function.
     *
     */
    @Test
    public void testMainNoargs() {
        final String[] sArguments = {};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, org.jastacry.Data.RC_ERROR);
    }

    /**
     * Test method unknown parameters error for Main function.
     *
     */
    @Test
    public void testMainUnknownargs() {
        final String[] sArguments = {"--unknown", "--arguments"};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, org.jastacry.Data.RC_ERROR);
    }

    /**
     * Test method missing parameters for Main function.
     *
     */
    @Test
    public void testMainMissingArgs() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sOutputFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;

        final String[] sArguments = {"--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, org.jastacry.Data.RC_ERROR);
    }

    /**
     * Test method encode call for Main function.
     *
     */
    @Test
    public void testMainEncode() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sOutputFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;

        final String[] sArguments = {"-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, 0);
    }

    /**
     * Test method decode call for Main function.
     *
     */
    @Test
    public void testMainDecode() {
        String sInputFile = "src/test/resources/" + INPUTENCODED;
        String sOutputFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;

        final String[] sArguments = {"-v", "--decode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", iRC, 0);
    }
}
