package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.layer.AbsCipherLayer;
import org.jastacry.layer.Md5DesLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testclass for MD5DES Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerMd5Des {
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * Layer to test.
     */
    private AbsCipherLayer layer = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "Passwort";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error.
     */
    @Before
    public void setUp() throws Exception {
        layer = new Md5DesLayer();
        layer.init(INITVALUE);
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case off error
     */
    @After
    public void tearDown() throws Exception {
        layer = null;
    }

    /**
     * Testcase testEncDecStream.
     *
     * @throws IOException
     *             in case off error
     */
    @Test
    // TestLink(externalId = "JAS-4")
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
    // TestLink(externalId = "JAS-5")
    public void testToString() {
        assertEquals("Layer name mismatch", Md5DesLayer.LAYERNAME, layer.toString());
    }

}
