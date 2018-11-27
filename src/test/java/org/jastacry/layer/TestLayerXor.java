package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.test.utils.Tooling;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Layer XOR.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerXor
{
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private XorLayer layer = null; // NOPMD by kkretsch on 29.03.18 14:54

    /**
     * Init value for xor layer.
     */
    private static final String INITVALUE = "123";

    /**
     * Test before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new XorLayer();
        layer.init(INITVALUE);
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
    // TestLink(externalId = "JAS-10")
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
     * Testcase testToString.
     */
    @Test
    // TestLink(externalId = "JAS-11")
    public void testToString()
    {
        assertEquals("Layer name mismatch", XorLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        XorLayer l1 = new XorLayer();
        XorLayer l2 = new XorLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer object equal", true, l1.equals(l2));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEquals()
    {
        XorLayer l1 = new XorLayer();
        XorLayer l2 = new XorLayer();
        l1.init(INITVALUE);
        l2.init("321");
        assertEquals("Layer object not equal", false, l1.equals(l2));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        XorLayer l1 = new XorLayer();
        l1.init(INITVALUE);
        assertEquals("Layer object same", true, l1.equals(l1));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        XorLayer l1 = new XorLayer();
        Object o = null;
        l1.init(INITVALUE);
        assertEquals("Layer object null unequal", false, l1.equals(o));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        XorLayer l1 = new XorLayer();
        Object o = new Object();
        l1.init(INITVALUE);
        assertEquals("Layer object null unequal", false, l1.equals(o));
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        XorLayer l1 = new XorLayer();
        XorLayer l2 = new XorLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
