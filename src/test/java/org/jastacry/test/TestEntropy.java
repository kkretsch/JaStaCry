package org.jastacry.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData;
import org.jastacry.JaStaCry;
import org.jastacry.test.utils.ShannonEntropy;
import org.jastacry.test.utils.Tooling;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of entropy.
 *
 * @author Kai Kretschmann
 *
 */
public class TestEntropy
{
    /**
     * Maven temp resources path.
     */
    private static final String TMPRESOURCES = "target/";

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
    private static Tooling tooling = null;

    /**
     * Tooling functions object.
     */
    private static ShannonEntropy shannon = null;

    /**
     * Test configuration file, contains broad range of running layers. used for "OK" tests.
     */
    public static final String CONF1 = "conf1.cfg";

    /**
     * Test input text file.
     */
    public static final String INPUTFILE = "plaintext.txt";

    /**
     * Test input binary file with all values.
     */
    public static final String INPUTBYTEFILE = "allbytes.dat";

    /**
     * Test input binary file with one value.
     */
    public static final String INPUTREPEATFILE = "onebyte.dat";

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
    private static File allbinFile;

    /**
     * temporary binary file.
     */
    private static File onebinFile;

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
    public static void setupData() throws MalformedURLException
    {
        oLogger = LogManager.getLogger();
        tooling = new Tooling();
        shannon = new ShannonEntropy();

        allbinFile = new File(TMPRESOURCES + INPUTBYTEFILE);
        tooling.createBinaryTestfile(allbinFile, 1024, (byte) 0x20);

        onebinFile = new File(TMPRESOURCES + INPUTREPEATFILE);
        tooling.createBinaryTestfile(onebinFile);
    }

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {

        try
        {
            tmpFile = File.createTempFile(org.jastacry.GlobalData.TMPBASE, GlobalData.TMPEXT);
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
    @After
    public void tearDown() throws Exception
    {
        encFile.delete();
        tmpFile.delete();
    }

    /**
     * Test method plain string.
     */
    @Test
    public void testEntropyZero()
    {
        String sSimple = "aaaaaaaaaa";
        shannon.calculate(sSimple);
        double entropy = shannon.getEntropy();
        oLogger.info("testEntropyZero Entropy: {}", entropy);
        assertTrue("Entropy", entropy == 0);
    }

    /**
     * Test method mixed string.
     *
     */
    @Test
    public void testEntropyNonzero()
    {
        String sSimple = "abcdefgh";
        shannon.calculate(sSimple);
        double entropy = shannon.getEntropy();
        oLogger.info("testEntropyNonzero Entropy: {}", entropy);
        assertTrue("Entropy", entropy > 0);
    }

    /**
     * Test method mixed byte array.
     *
     */
    @Test
    public void testEntropyBytes()
    {
        String sSimple = "abcdefgh";
        byte[] byteArr = sSimple.getBytes();
        shannon.calculate(byteArr);
        double entropy = shannon.getEntropy();
        oLogger.info("testEntropyBytes Entropy: {}", entropy);
        assertTrue("Entropy", entropy > 0);
    }

    /**
     * Test method mixed byte array.
     *
     */
    @Test
    public void testEntropyStringEqualsBytes()
    {
        String sSimple = "abcdefgh";

        shannon.calculate(sSimple);
        double entropyString = shannon.getEntropy();

        byte[] byteArr = sSimple.getBytes();
        shannon.calculate(byteArr);
        double entropyBytes = shannon.getEntropy();
        oLogger.info("testEntropyStringEqualsBytes Entropy: {} & {}", entropyString, entropyBytes);
        assertTrue("Entropy equals", entropyString == entropyBytes);
    }

    /**
     * Test method encode call for Main function.
     *
     */
    @Test
    public void testEntropyMainEncode()
    {
        final String sInputFile = RESOURCES + INPUTFILE;
        final String sOutputFile = encFile.getAbsolutePath();
        final String sConfigFile = RESOURCES + CONF1;

        final String[] sArguments = {
            "-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile", sConfigFile
        };
        oLogger.info("Main test with args: {}", Arrays.toString(sArguments));
        final int iRc = JaStaCry.mainMethod(sArguments);
        assertEquals("Main encode returncode", 0, iRc);

        // Test entropy values
        try
        {
            Path pathInput = Paths.get(sInputFile);
            byte[] dataInput = Files.readAllBytes(pathInput);
            shannon.calculate(dataInput);
            double entropyInput = shannon.getEntropy();

            Path pathOutput = Paths.get(sOutputFile);
            byte[] dataOutput = Files.readAllBytes(pathOutput);
            shannon.calculate(dataOutput);
            double entropyOutput = shannon.getEntropy();

            oLogger.info("testMainEncode Entropy: {} to {}", entropyInput, entropyOutput);

            assertTrue("Entropy input above zero", entropyInput > 0);
            assertTrue("Entropy output above zero", entropyOutput > 0);
            assertTrue("Entropy getting better", entropyInput < entropyOutput);
        }
        catch (IOException e)
        {
            oLogger.catching(e);
        }
    }
}
