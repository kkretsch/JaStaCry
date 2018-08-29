package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.jastacry.JastacryException;
import org.jastacry.test.utils.Tooling;
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
     * @throws JastacryException
     *             in case of error
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-6")
    public void testEncDecStreamLong() throws JastacryException, IOException
    {
        InputStream is = new FileInputStream(LONGTEXTFILE);
        byte[] buf = IOUtils.toByteArray(is);

        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        byte[] buf2 = osEncode.toByteArray();

        layer = null;
        layer = new FilemergeLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf2);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.decStream(isDecode, osDecode);

        String sTextcontent = new String(buf, "ISO-8859-1");
        assertEquals("decoding differs", sTextcontent, osDecode.toString());
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
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", FilemergeLayer.LAYERNAME, layer.toString());
    }

}
