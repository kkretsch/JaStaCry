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
 * Test of Layer Transparent.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerTransparent
{
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private TransparentLayer layer = null;

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        layer = new TransparentLayer();
        layer.init("");
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
     * Test method for {@link org.jastacry.layer.TransparentLayer#encStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     */
    @Test
    public void testEncStream() throws JastacryException
    {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        final OutputStream os = new ByteArrayOutputStream();
        layer.encStream(is, os);
        assertEquals("encoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.TransparentLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     */
    @Test
    public void testDecStream() throws JastacryException
    {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        final OutputStream os = new ByteArrayOutputStream();
        layer.decStream(is, os);
        assertEquals("decoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.TransparentLayer#toString()}.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", TransparentLayer.LAYERNAME, layer.toString());
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
     * Testcase testDecStream Exceptions.
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
        TransparentLayer l1 = new TransparentLayer();
        TransparentLayer l2 = new TransparentLayer();
        assertEquals("Layer object equal", l1, l2);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        TransparentLayer l1 = new TransparentLayer();
        assertEquals("Layer object same", l1, l1);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        TransparentLayer l1 = new TransparentLayer();
        assertEquals("Layer object null unequal", false, l1.equals(null));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        TransparentLayer l1 = new TransparentLayer();
        Object o = new Object();
        assertEquals("Layer object null unequal", false, l1.equals(o));
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        TransparentLayer l1 = new TransparentLayer();
        TransparentLayer l2 = new TransparentLayer();
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
