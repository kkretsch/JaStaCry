package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.test.utils.Tooling;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for MD5DES Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerMd5Des
{
    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * Layer to test.
     */
    private Md5DesLayer layerEncrypt = null;

    /**
     * Layer to test.
     */
    private Md5DesLayer layerDecrypt = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "Passwort123";

    /**
     * Tooling functions object.
     */
    private Tooling tooling;

    /**
     * The BeforeClass method.
     *
     * @throws MalformedURLException
     *             in case of error.
     */
    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        oLogger = LogManager.getLogger();
    }

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error.
     */
    @Before
    public void setUp() throws Exception
    {
        tooling = new Tooling();

        layerEncrypt = new Md5DesLayer();
        layerEncrypt.init(INITVALUE);
        layerDecrypt = new Md5DesLayer();
        layerDecrypt.init(INITVALUE);
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case off error
     */
    @After
    public void tearDown() throws Exception
    {
        layerEncrypt = null;
        layerDecrypt = null;
    }

    /**
     * Testcase testEncDecStream.
     *
     * @throws IOException
     *             in case off error
     */
    @Test
    // TestLink(externalId = "JAS-4")
    public void testEncDecStream() throws IOException
    {
        // tooling.listProviders();

        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layerEncrypt.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layerDecrypt.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());
    }

    /**
     * Testcase testToString.
     */
    @Test
    // TestLink(externalId = "JAS-5")
    public void testToString()
    {
        assertEquals("Layer name mismatch", Md5DesLayer.LAYERNAME, layerEncrypt.toString());
    }

}
