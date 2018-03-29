package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Logger;
import org.jastacry.layer.BasicLayer;
import org.jastacry.layer.ReverseLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerReverse {
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * The layer to test.
     */
    private BasicLayer layer = null; // NOPMD by kkretsch on 29.03.18 14:55

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception {
        layer = new ReverseLayer();
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case of error
     */
    @After
    public void tearDown() throws Exception {
        layer = null;
    }

    /**
     * Test case testEncDecStream.
     *
     * @throws IOException
     *             in case of error
     */
    @Test
    public void testEncDecStream() throws IOException {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());
    }

    /**
     * Test case testToString.
     */
    @Test
    public void testToString() {
        assertEquals("Layer name mismatch", ReverseLayer.LAYERNAME, layer.toString());
    }

}
