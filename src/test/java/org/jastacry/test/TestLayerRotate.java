package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.layer.RotateLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author kkre
 *
 */
public class TestLayerRotate {
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private RotateLayer layer = null;

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception {
        layer = new RotateLayer();
        layer.init("2");
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
     * Testcase testEncDecStream.
     *
     * @throws IOException
     *             in case of error
     */
    @Test
    // TestLink(externalId = "JAS-8")
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
     * Testcase testToString.
     */
    @Test
    // TestLink(externalId = "JAS-9")
    public void testToString() {
        assertEquals("Layer name mismatch", RotateLayer.LAYERNAME, layer.toString());
    }

}
