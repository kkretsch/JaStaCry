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
public class TestMainBytes
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
     * Test configuration file, contains broad range of running layers. used for "OK" tests.
     */
    public static final String CONF_BYTES = "conf_bytes.cfg";


    /**
     * Test input binary file.
     */
    public static final String INPUTBYTEFILE = "allbytes.dat";

    /**
     * temporary file.
     */
    private File tmpFile; // NOPMD by kkretsch on 29.03.18 14:53

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
        try
        {
            tmpFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
            encFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.ENCEXT);
            oLogger.info("enc file {}, tmp file {}", encFile.getAbsolutePath(), tmpFile.getAbsolutePath());
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
//        encFile.delete();
//        tmpFile.delete();
    }

    /**
     * Test method encode call for Main function.
     *
     */
    @Test
    public void testMainEncode()
    {
        String sInputFile = RESOURCES + INPUTBYTEFILE;
        String sOutputFile = encFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF_BYTES;

        final String[] sArgumentsEnc = {
            "-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile
        };
        oLogger.info("Main test with args: {}", Arrays.toString(sArgumentsEnc));
        int returncode = JaStaCry.mainMethod(sArgumentsEnc);
        assertEquals("Main encode returncode", 0, returncode);

        // now backwards
        sInputFile = encFile.getAbsolutePath();
        sOutputFile = tmpFile.getAbsolutePath();

        final String[] sArgumentsDec = {
            "-v", "--decode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile
        };
        oLogger.info("Main test with args: {}", Arrays.toString(sArgumentsDec));
        returncode = JaStaCry.mainMethod(sArgumentsDec);
        assertEquals("Main decode returncode", 0, returncode);
    }

}
