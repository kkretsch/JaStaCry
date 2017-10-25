/**
 *
 */
package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.EncodeDecodeLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kai Kretschmann
 *
 */
public class TestLayerEncodeDecode {
    private static Logger LOGGER = null;

    private final String testdata = "The quick brown fox jumps over the lazy dog.";
    private EncodeDecodeLayer layer = null;

    @BeforeClass
    public static void setLogger() throws MalformedURLException {
        LOGGER = LogManager.getLogger();
    }

    @Before
    public void setUp() throws Exception {
        layer = new EncodeDecodeLayer();
    }

    @After
    public void tearDown() throws Exception {
        layer = null;
    }

    /**
     * Test method for {@link org.jastacry.layer.EncodeDecodeLayer#encStream(java.io.InputStream, java.io.OutputStream)}
     * .
     *
     * @throws IOException
     */
    @Test
    // TestLink(externalId = "JAS-12")
    public void testEncStream() throws IOException {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        LOGGER.debug("'" + is.toString() + "'");
        final OutputStream os = new ByteArrayOutputStream();
        layer.encStream(is, os);
        LOGGER.debug("'" + os.toString() + "'");
        assertEquals("encoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.EncodeDecodeLayer#decStream(java.io.InputStream, java.io.OutputStream)}
     * .
     *
     * @throws IOException
     */
    @Test
    // TestLink(externalId = "JAS-13")
    public void testDecStream() throws IOException {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        final OutputStream os = new ByteArrayOutputStream();
        layer.decStream(is, os);
        assertEquals("decoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.EncodeDecodeLayer#toString()}.
     */
    @Test
    // TestLink(externalId = "JAS-14")
    public void testToString() {
        assertEquals("Layer name mismatch", EncodeDecodeLayer.LAYERNAME, layer.toString());
    }

}
