/**
 *
 */
package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData;
import org.jastacry.GlobalData.Returncode;
import org.jastacry.test.utils.Tooling;
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
public class TestConfig {
    /**
     * Maven test resources path
     */
    private final static String RESOURCES = "src/test/resources/";

    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * Tooling functions object.
     */
    private Tooling tooling;

    /**
     * Test configuration file, UTF8 encoded with BOM.
     */
    public static final String CONF_UTFBOM = "conf_withbom.cfg";

    /**
     * Test configuration file, UTF8 encoded without BOM.
     */
    public static final String CONF_UTFNOBOM = "conf_withoutbom.cfg";

    /**
     * Test configuration file, ISO encoded and for sure no BOM.
     */
    public static final String CONF_ISO = "conf_withiso.cfg";

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
    private File tmpFile;

    /**
     * temporary binary file.
     */
    private File binFile;

    /**
     * Encrypted file.
     */
    private File encFile;

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
        tooling = new Tooling();
        try {
            tmpFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
            binFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
            encFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.ENCEXT);
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
        encFile.delete();
        binFile.delete();
        tmpFile.delete();
    }

    private void testGivenConfig(final String sConfig) {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sDecryptedFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + sConfig;
        final File fInputfile = new File(sInputFile);
        final File fEncryptedfile = new File(sEncryptedFile);
        final File fDecryptedfile = new File(sDecryptedFile);

        final String[] sArgumentsEncrypt = { "--encode", "--infile", sInputFile, "--outfile", sEncryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main ascencdec returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("Encrypted data content", fEncryptedfile.length() > 0);

        final String[] sArgumentsDecrypt = { "-v", "--decode", "--infile", sEncryptedFile, "--outfile", sDecryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test decrypt with args: {}", Arrays.toString(sArgumentsDecrypt));
        iRC = JaStaCry.mainMethod(sArgumentsDecrypt);
        assertEquals("Main ascdecend returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("File results in equal content", tooling.compareFiles(fInputfile, fDecryptedfile));
    }

    /**
     * Test method normal for Main function.
     *
     */
    @Test
    public void testConfWithBOM() {
        testGivenConfig(CONF_UTFBOM);
    }

    /**
     * Test method normal for Main function.
     *
     */
    @Test
    public void testConfWithoutBOM() {
        testGivenConfig(CONF_UTFNOBOM);
    }

    /**
     * Test method normal for Main function.
     *
     */
    @Test
    public void testConfIso() {
        testGivenConfig(CONF_ISO);
    }

}
