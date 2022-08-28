package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData;
import org.jastacry.JaStaCry;
import org.jastacry.test.utils.Tooling;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestMainSingleDecode
{
    /**
     * Maven test resources path.
     */
    private static final String RESOURCES = "src/test/resources/";

    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * Tooling functions object.
     */
    private Tooling tooling;

    /**
     * Test configuration file, contains broad range of running layers. used for "OK" tests.
     */
    public static final String CONF1 = "conf1.cfg";


    /**
     * Test input text file.
     */
    public static final String INPUTFILE = "plaintext.txt";

    /**
     * Test input binary file.
     */
    public static final String INPUTBYTEFILE = "allbytes.dat";

    /**
     * Test input encoded file.
     */
    public static final String INPUTENCODED = "encoded.dat";

    /**
     * temporary file.
     */
    private File tmpFile; // NOPMD by kkretsch on 29.03.18 14:53

    /**
     * temporary binary file.
     */
    private File binFile; // NOPMD by kkretsch on 29.03.18 14:53

    /**
     * Encrypted file.
     */
    private File encFile; // NOPMD by kkretsch on 29.03.18 14:53


    /**
     * The BeforeClass method.
     *
     * @throws MalformedURLException
     *             in case of error.
     */
    @BeforeAll
    public static void setLogger() throws MalformedURLException
    {
        oLogger = LogManager.getLogger();
    }

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        tooling = new Tooling();
        try
        {
            tmpFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
            binFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
            encFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.ENCEXT);
        }
        catch (final IOException e1)
        {
            oLogger.catching(e1);
        }
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case of error
     */
    @AfterEach
    public void tearDown() throws Exception
    {
        encFile.delete();
        binFile.delete();
        tmpFile.delete();
    }

    /**
     * Test method decode call for Main function.
     *
     */
    @Test
    public void testMainDecode()
    {
        final String sInputFile = RESOURCES + INPUTENCODED;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArguments = {
            "-v", "--decode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile
        };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int returncode = JaStaCry.mainMethod(sArguments);
        assertEquals("Main decode returncode", 0, returncode);
    }

}
