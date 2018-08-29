package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.layer.RandomLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Layer Random.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerRandom
{
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private RandomLayer layer = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "333";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new RandomLayer();
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
        layer = null;
    }

    /**
     * Testcase testEncDecStream.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    // TestLink(externalId = "JAS-6")
    public void testEncDecStream() throws JastacryException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new RandomLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());

    }

    /**
     * Testcase testToString.
     */
    @Test
    // TestLink(externalId = "JAS-7")
    public void testToString()
    {
        assertEquals("Layer name mismatch", RandomLayer.LAYERNAME, layer.toString());
    }

}
