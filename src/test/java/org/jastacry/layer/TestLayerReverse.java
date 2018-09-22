package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Logger;
import org.jastacry.JastacryException;
import org.jastacry.layer.ReverseLayer;
import org.jastacry.test.utils.Tooling;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerReverse
{
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
    private Layer layer = null;

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new ReverseLayer();
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
     * Test case testEncDecStream.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testEncDecStream() throws JastacryException
    {
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
    public void testToString()
    {
        assertEquals("Layer name mismatch", ReverseLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase testEncStream Exceptions.
     *
     * @throws JastacryException
     *             in case of error
     * @throws IOException will be thrown in test
     */
    @Test(expected = JastacryException.class)
    public void testEncStreamException() throws JastacryException, IOException
    {
        Tooling tool = new Tooling();
        tool.mockupInputOutputEncStreams(layer);
    }

    /**
     * Testcase testDecStream Exceptions.
     *
     * @throws JastacryException
     *             in case of error
     * @throws IOException will be thrown in test
     */
    @Test(expected = JastacryException.class)
    public void testDecStreamException() throws JastacryException, IOException
    {
        Tooling tool = new Tooling();
        tool.mockupInputOutputDecStreams(layer);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        ReverseLayer l1 = new ReverseLayer();
        ReverseLayer l2 = new ReverseLayer();
        assertEquals("Layer equal", l1, l2);
    }
}
