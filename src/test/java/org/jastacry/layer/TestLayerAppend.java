package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.JastacryException;
import org.jastacry.test.utils.Tooling;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test of Layer Random.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerAppend
{
    private final Logger logger = LogManager.getLogger();

    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private AppendLayer layer = null;

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
    @BeforeEach
    public void setUp() throws Exception
    {
        layer = new AppendLayer();
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
     * Testcase testEncDecStream.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testEncDecStream() throws JastacryException
    {
        logger.debug("test enc " + INITVALUE);
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new AppendLayer();
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
        layer = new AppendLayer();
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
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", AppendLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        AppendLayer l1 = new AppendLayer();
        AppendLayer l2 = new AppendLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer object equal", l1, l2);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        AppendLayer l1 = new AppendLayer();
        l1.init(INITVALUE);
        assertEquals("Layer object same", l1, l1);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        AppendLayer l1 = new AppendLayer();
        l1.init(INITVALUE);
        Object o = null;
        assertEquals("Layer object null unequal", l1.equals(o), false);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        AppendLayer l1 = new AppendLayer();
        l1.init(INITVALUE);
        Object o = new Object();
        assertEquals("Layer object wrong class unequal", l1.equals(o), false);
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        AppendLayer l1 = new AppendLayer();
        AppendLayer l2 = new AppendLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }
}
