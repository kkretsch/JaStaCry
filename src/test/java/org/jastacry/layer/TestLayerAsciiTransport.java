package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.JastacryException;
import org.jastacry.layer.AsciiTransportLayer;
import org.jastacry.test.utils.Tooling;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for ascii transport format.
 * 
 * @author Kai Kretschmann
 *
 */
public class TestLayerAsciiTransport
{
    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * default data plain.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * default data encoded.
     */
    private final String testdataEncoded = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";

    /**
     * The layer object to test.
     */
    private AsciiTransportLayer layer = null;

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
     * The Before method.
     *
     * @throws Exception
     *             in case of error.
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new AsciiTransportLayer();
        layer.init("");
    }

    /**
     * The After method.
     *
     * @throws Exception
     *             in case of error.
     */
    @After
    public void tearDown() throws Exception
    {
        layer = null;
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#encStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException i case of error
     */
    @Test
    // TestLink(externalId = "JAS-12")
    public void testEncStream() throws JastacryException, IOException
    {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testEncStream is='{}'", text);
        oLogger.debug("size of input text is {}", testdata.length());
        final OutputStream os = new ByteArrayOutputStream();
        layer.encStream(is, os);
        os.flush();
        oLogger.debug("testEncStream os='{}'", os.toString());
        assertEquals("encoding differs", testdataEncoded, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-13")
    public void testDecStream() throws JastacryException, IOException
    {
        final byte[] buf = testdataEncoded.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testDecStream is='{}'", text);
        oLogger.debug("size of encoded text is {}", testdataEncoded.length());
        final OutputStream os = new ByteArrayOutputStream();
        layer.decStream(is, os);
        oLogger.debug("testDecStream os='{}'", os.toString());
        assertEquals("decoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#toString()} .
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", AsciiTransportLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase unsupported exception.
     * @throws JastacryException on error
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() throws JastacryException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.encodeAndDecode(isEncode, osEncode);
    }

}
