package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.jastacry.JastacryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Rotate Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerReadWrite
{
    /**
     * Testdata to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private ReadWriteLayer layer = null;

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new ReadWriteLayer();
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
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", ReadWriteLayer.LAYERNAME, layer.toString());
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

    /**
     * Testcase testInit.
     */
    @Test
    public void testInit()
    {
        layer.init("");
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        ReadWriteLayer l1 = new ReadWriteLayer();
        ReadWriteLayer l2 = new ReadWriteLayer();
        assertEquals("Layer object equal", l1, l2);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        ReadWriteLayer l1 = new ReadWriteLayer();
        assertEquals("Layer object same", l1, l1);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        ReadWriteLayer l1 = new ReadWriteLayer();
        Object o = null;
        assertEquals("Layer object null unequal", l1.equals(o), false);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        ReadWriteLayer l1 = new ReadWriteLayer();
        Object o = new Object();
        assertEquals("Layer object wrong class unequal", l1.equals(o), false);
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        ReadWriteLayer l1 = new ReadWriteLayer();
        ReadWriteLayer l2 = new ReadWriteLayer();
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
