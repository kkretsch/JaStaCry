/**
 *
 */
package org.jastacry.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData;
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
     * Tooling functions object.
     */
    private Tooling tooling;

    /**
     * Test configuration file.
     */
    public static final String CONF1 = "conf1.cfg";

    /**
     * Test configuration file.
     */
    public static final String CONF2 = "conf2.cfg";

    /**
     * Test configuration file.
     */
    public static final String CONF3 = "conf3.cfg";

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

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testMainHelp() {
        final String[] sArguments = {"-h"};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", GlobalData.RC_HELP, iRC);
    }

    /**
     * Test method no parameters error for Main function.
     *
     */
    @Test
    public void testMainNoargs() {
        final String[] sArguments = {};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main noargs returncode", GlobalData.RC_ERROR, iRC);
    }

    /**
     * Test method unknown parameters error for Main function.
     *
     */
    @Test
    public void testMainUnknownargs() {
        final String[] sArguments = {"--unknown", "--arguments"};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main unknown args returncode", GlobalData.RC_ERROR, iRC);
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
        assertEquals("Main missing args returncode", GlobalData.RC_ERROR, iRC);
    }

    /**
     * Test method encode call for Main function.
     *
     */
    @Test
    public void testMainEncode() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sOutputFile = encFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;

        final String[] sArguments = {"-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main encode returncode", 0, iRC);
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

        final String[] sArguments = {"--decode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main decode returncode", 0, iRC);
    }

    /**
     * Test method one layer for Main function.
     *
     */
    @Test
    public void testMainOnelayer() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sOutputFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF2;

        final String[] sArguments = {"--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main one layer returncode", GlobalData.RC_ERROR, iRC);
    }

    /**
     * Test method one layer for Main function.
     *
     */
    @Test
    public void testMainNolayer() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sOutputFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF3;

        final String[] sArguments = {"-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile};
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main no layer returncode", GlobalData.RC_ERROR, iRC);
    }

    /**
     * Test method one layer for Main function.
     *
     */
    @Test
    public void testMainEncDec() {
        String sInputFile = "src/test/resources/" + INPUTFILE;
        String sEncryptedFile = encFile.getAbsolutePath();
        String sDecryptedFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;
        File fInputfile = new File(sInputFile);
        File fEncryptedfile = new File(sEncryptedFile);
        File fDecryptedfile = new File(sDecryptedFile);

        final String[] sArgumentsEncrypt =
            {"--encode", "--infile", sInputFile, "--outfile", sEncryptedFile, "--conffile", sConfigFile};
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main ascencdec returncode", GlobalData.RC_OK, iRC);

        assertTrue("Encrypted data content", fEncryptedfile.length() > 0);

        final String[] sArgumentsDecrypt =
            {"-v", "--decode", "--infile", sEncryptedFile, "--outfile", sDecryptedFile, "--conffile", sConfigFile};
        oLogger.info("Main test decrypt with args: {}", Arrays.toString(sArgumentsDecrypt));
        iRC = JaStaCry.mainMethod(sArgumentsDecrypt);
        assertEquals("Main ascdecend returncode", iRC, GlobalData.RC_OK, iRC);

        assertTrue("File results in equal content", tooling.compareFiles(fInputfile, fDecryptedfile));
    }

    /**
     * Test method binary data for Main function.
     *
     */
    @Test
    public void testMainBinaryEncDec() {
        tooling.createBinaryTestfile(binFile);

        String sInputFile = binFile.getAbsolutePath();
        String sEncryptedFile = encFile.getAbsolutePath();
        String sDecryptedFile = tmpFile.getAbsolutePath();
        String sConfigFile = "src/test/resources/" + CONF1;
        File fInputfile = new File(sInputFile);
        File fEncryptedfile = new File(sEncryptedFile);
        File fDecryptedfile = new File(sDecryptedFile);

        final String[] sArgumentsEncrypt =
            {"-v", "--encode", "--infile", sInputFile, "--outfile", sEncryptedFile, "--conffile", sConfigFile};
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main binencdec returncode", GlobalData.RC_OK, iRC);

        assertTrue("Encrypted data content", fEncryptedfile.length() > 0);

        final String[] sArgumentsDecrypt =
            {"-v", "--decode", "--infile", sEncryptedFile, "--outfile", sDecryptedFile, "--conffile", sConfigFile};
        oLogger.info("Main test decrypt with args: {}", Arrays.toString(sArgumentsDecrypt));
        iRC = JaStaCry.mainMethod(sArgumentsDecrypt);
        assertEquals("Main bindecenc returncode", iRC, GlobalData.RC_OK, iRC);

        assertTrue("File results in equal content", tooling.compareFiles(fInputfile, fDecryptedfile));
    }

}
