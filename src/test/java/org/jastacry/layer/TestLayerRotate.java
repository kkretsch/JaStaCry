package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.test.utils.Tooling;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    @BeforeEach
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
    @AfterEach
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
    @Test
    // TestLink(externalId = "JAS-9")
    public void testUnsupported() throws JastacryException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            layer.encodeAndDecode(isEncode, osEncode);
        });
    }

    /**
     * Testcase testEncStream Exceptions.
     *
     * @throws JastacryException
     *             in case of error
     * @throws IOException will be thrown in test
     */
    @Test
    public void testEncStreamException() throws JastacryException, IOException
    {
        Tooling tool = new Tooling();
        Assertions.assertThrows(JastacryException.class, () -> {
            tool.mockupInputOutputEncStreams(layer);
        });
    }

    /**
     * Testcase testEncStream Exceptions.
     *
     * @throws JastacryException
     *             in case of error
     * @throws IOException will be thrown in test
     */
    @Test
    public void testDecStreamException() throws JastacryException, IOException
    {
        Tooling tool = new Tooling();
        Assertions.assertThrows(JastacryException.class, () -> {
            tool.mockupInputOutputDecStreams(layer);
        });
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
        assertEquals("Layer object equal", true, l1.equals(l2));
    }

    /**
     * Testcase not equals.
     */
    @Test
    public void testNotEquals()
    {
        RotateLayer l1 = new RotateLayer();
        RotateLayer l2 = new RotateLayer();
        l1.init(SHIFTSMALL);
        l2.init(SHIFTWIDE);
        assertEquals("Layer object not equal", false, l1.equals(l2));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        RotateLayer l1 = new RotateLayer();
        l1.init(SHIFTSMALL);
        assertEquals("Layer object same", true, l1.equals(l1));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        RotateLayer l1 = new RotateLayer();
        Object o = null;
        l1.init(SHIFTSMALL);
        assertEquals("Layer object null unequal", false, l1.equals(o));
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
        assertEquals("Layer object type unequal", false, l1.equals(o));
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
