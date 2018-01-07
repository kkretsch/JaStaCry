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
public class TestMain {
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
     * Test configuration file, contains broad range of running layers. used for "OK" tests.
     */
    public static final String CONF1 = "conf1.cfg";

    /**
     * Test configuration file, contains unknown tag for tests.
     */
    public static final String CONF2 = "conf2.cfg";

    /**
     * Test configuration file, contains no layer at all.
     */
    public static final String CONF3 = "conf3.cfg";

    /**
     * Test configuration file, contains two layers.
     */
    public static final String CONF4 = "conf4.cfg";

    /**
     * Test configuration file, contains only one layers.
     */
    public static final String CONF5 = "conf5.cfg";

    /**
     * Test configuration file, contains interactive password macro.
     */
    public static final String CONF6 = "conf6.cfg";

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
        final String[] sArguments = { "-h" };
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main help returncode", Returncode.RC_HELP.getNumVal(), iRC);
    }

    /**
     * Test method no parameters error for Main function.
     *
     */
    @Test
    public void testMainNoargs() {
        final String[] sArguments = {};
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main noargs returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method unknown parameters error for Main function.
     *
     */
    @Test
    public void testMainUnknownargs() {
        final String[] sArguments = { "--unknown", "--arguments" };
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main unknown args returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method missing parameters for Main function.
     *
     */
    @Test
    public void testMainMissingArgs() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArguments = { "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main missing args returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method missing parameters for Main function.
     *
     */
    @Test
    public void testMainMissingParams() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();

        final String[] sArguments = { "--infile", sInputFile, "--outfile", sOutputFile, "--conffile" };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main missing args returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method encode call for Main function.
     *
     */
    @Test
    public void testMainEncode() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = encFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArguments = { "-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
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
        final String sInputFile = RESOURCES + INPUTENCODED;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArguments = { "--decode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main decode returncode", 0, iRC);
    }

    /**
     * Test method unknown layer for Main function.
     *
     */
    @Test
    public void testMainUnknownLayer() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF2;

        final String[] sArguments = { "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main one layer returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method one layer for Main function.
     *
     */
    @Test
    public void testMainOnelayer() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF5;

        final String[] sArguments = { "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main one layer returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method two layer for Main function.
     *
     */
    @Test
    public void testMainTwolayer() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF4;

        final String[] sArguments = { "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main two layer returncode", Returncode.RC_OK.getNumVal(), iRC);
    }

    /**
     * Test method two layer for Main function.
     *
     */
    @Test
    public void testMainPassword() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF6;

        final String[] sArguments = { "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main two layer returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method no layer for Main function.
     *
     */
    @Test
    public void testMainNolayer() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF3;

        final String[] sArguments = { "-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRC = JaStaCry.mainMethod(sArguments);
        assertEquals("Main no layer returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method missing input file for Main function.
     *
     */
    @Test
    public void testMainMissingInputFile() {
        final String sInputFile = RESOURCES + "NotExistingFile.txt";
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArgumentsEncrypt = { "--encode", "--infile", sInputFile, "--outfile", sEncryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        final int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main testMainMissingInputFile returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method missing config file for Main function.
     *
     */
    @Test
    public void testMainMissingConfigFile() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + "NotExistingConfig.txt";

        final String[] sArgumentsEncrypt = { "--encode", "--infile", sInputFile, "--outfile", sEncryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        final int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main testMainMissingConfigFile returncode", Returncode.RC_ERROR.getNumVal(), iRC);
    }

    /**
     * Test method normal for Main function.
     *
     */
    @Test
    public void testMainEncDec() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sDecryptedFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;
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
     * Test method normal for Main function including base64 encoding
     *
     */
    @Test
    public void testMainBase64EncDec() {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sDecryptedFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF4;
        final File fInputfile = new File(sInputFile);
        final File fEncryptedfile = new File(sEncryptedFile);
        final File fDecryptedfile = new File(sDecryptedFile);

        final String[] sArgumentsEncrypt = { "--encode", "--text", "--infile", sInputFile, "--outfile", sEncryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main ascencdec returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("Encrypted data content", fEncryptedfile.length() > 0);

        final String[] sArgumentsDecrypt = { "-v", "--decode", "--text", "--infile", sEncryptedFile, "--outfile",
                sDecryptedFile, "--conffile", sConfigFile };
        oLogger.info("Main test decrypt with args: {}", Arrays.toString(sArgumentsDecrypt));
        iRC = JaStaCry.mainMethod(sArgumentsDecrypt);
        assertEquals("Main ascdecend returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("File results in equal content", tooling.compareFiles(fInputfile, fDecryptedfile));
    }

    /**
     * Test method binary data for Main function.
     *
     */
    @Test
    public void testMainBinaryEncDec() {
        tooling.createBinaryTestfile(binFile);

        final String sInputFile = binFile.getAbsolutePath();
        final String sEncryptedFile = encFile.getAbsolutePath();
        final String sDecryptedFile = tmpFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;
        final File fInputfile = new File(sInputFile);
        final File fEncryptedfile = new File(sEncryptedFile);
        final File fDecryptedfile = new File(sDecryptedFile);

        final String[] sArgumentsEncrypt = { "-v", "--encode", "--infile", sInputFile, "--outfile", sEncryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test encrypt with args: {}", Arrays.toString(sArgumentsEncrypt));
        int iRC = JaStaCry.mainMethod(sArgumentsEncrypt);
        assertEquals("Main binencdec returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("Encrypted data content", fEncryptedfile.length() > 0);

        final String[] sArgumentsDecrypt = { "-v", "--decode", "--infile", sEncryptedFile, "--outfile", sDecryptedFile,
                "--conffile", sConfigFile };
        oLogger.info("Main test decrypt with args: {}", Arrays.toString(sArgumentsDecrypt));
        iRC = JaStaCry.mainMethod(sArgumentsDecrypt);
        assertEquals("Main bindecenc returncode", Returncode.RC_OK.getNumVal(), iRC);

        assertTrue("File results in equal content", tooling.compareFiles(fInputfile, fDecryptedfile));
    }

}
