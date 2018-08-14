package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.jastacry.layer.FilemergeLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Layer Random.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerFilemerge
{
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private FilemergeLayer layer = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "src/test/resources/foto.jpg";

    /**
     * Init value for random layer.
     */
    private static final String LONGTEXTFILE = "src/test/resources/longtext.txt";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new FilemergeLayer();
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
     * @throws IOException
     *             in case of error
     */
    @Test
    // TestLink(externalId = "JAS-6")
    public void testEncDecStream() throws IOException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new FilemergeLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());
    }

    /**
     * Testcase testEncDecStream.
     *
     * @throws IOException
     *             in case of error
     */
    @Test
    // TestLink(externalId = "JAS-6")
    public void testEncDecStreamLong() throws IOException
    {
        InputStream is = new FileInputStream(LONGTEXTFILE);
        byte[] buf = IOUtils.toByteArray(is);
        String sTextcontent = new String(buf, "ISO-8859-1");
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new FilemergeLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", sTextcontent, osDecode.toString());
    }

    /**
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", FilemergeLayer.LAYERNAME, layer.toString());
    }

}
