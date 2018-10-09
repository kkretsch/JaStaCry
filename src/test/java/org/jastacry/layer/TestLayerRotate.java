package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.layer.RotateLayer;
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
public class TestLayerRotate
{
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private RotateLayer layer = null; // NOPMD by kkretsch on 29.03.18 14:54

    /**
     * Shift a small amount.
     */
    private static final String SHIFTSMALL = "2";

    /**
     * Shift a wide amount.
     */
    private static final String SHIFTWIDE = "250";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new RotateLayer();
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
     * Testcase JastacryException.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testEncDecStream() throws JastacryException
    {
        layer.init(SHIFTSMALL);
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
     * Testcase testEncDecStreamWide.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testEncDecStreamWide() throws JastacryException
    {
        layer.init(SHIFTWIDE);
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
    public void testToString()
    {
        assertEquals("Layer name mismatch", RotateLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase unsupported exception.
     * @throws JastacryException on error
     */
    @Test(expected = UnsupportedOperationException.class)
    // TestLink(externalId = "JAS-9")
    public void testUnsupported() throws JastacryException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.encodeAndDecode(isEncode, osEncode);
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
     * Testcase testEncStream Exceptions.
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
        RotateLayer l1 = new RotateLayer();
        RotateLayer l2 = new RotateLayer();
        l1.init(SHIFTSMALL);
        l2.init(SHIFTSMALL);
        assertEquals("Layer object equal", l1, l2);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        RotateLayer l1 = new RotateLayer();
        l1.init(SHIFTSMALL);
        assertEquals("Layer object null unequal", l1.equals(null), false);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        RotateLayer l1 = new RotateLayer();
        Object o = new Object();
        l1.init(SHIFTSMALL);
        assertEquals("Layer object null unequal", l1.equals(o), false);
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        RotateLayer l1 = new RotateLayer();
        RotateLayer l2 = new RotateLayer();
        l1.init(SHIFTSMALL);
        l2.init(SHIFTSMALL);
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
