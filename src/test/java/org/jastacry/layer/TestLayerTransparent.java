package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.layer.TransparentLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    @Before
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
    @After
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
    // TestLink(externalId = "JAS-1")
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
    // TestLink(externalId = "JAS-2")
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
    // TestLink(externalId = "JAS-3")
    public void testToString()
    {
        assertEquals("Layer name mismatch", TransparentLayer.LAYERNAME, layer.toString());
    }

}
