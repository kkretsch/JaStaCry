package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData.Action;
import org.jastacry.JastacryException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for ascii transport format.
 * 
 * @author Kai Kretschmann
 *
 */
public class TestLayerAsciiTransport
{
    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * default data plain.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * default data encoded.
     */
    private final String testdataEncoded = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";

    /**
     * The layer object to test.
     */
    private AsciiTransportLayer layer = null;

    private CountDownLatch endController = new CountDownLatch(2);
    
    /**
     * The BeforeClass method.
     *
     * @throws MalformedURLException
     *             in case of error.
     */
    @BeforeAll
    public static void setLogger() throws MalformedURLException
    {
        oLogger = LogManager.getLogger();
    }

    /**
     * The Before method.
     *
     * @throws Exception
     *             in case of error.
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        layer = new AsciiTransportLayer();
        layer.init("");
    }

    /**
     * The After method.
     *
     * @throws Exception
     *             in case of error.
     */
    @AfterEach
    public void tearDown() throws Exception
    {
        layer = null;
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#encStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException i case of error
     */
    @Test
    // TestLink(externalId = "JAS-12")
    public void testEncStream() throws JastacryException, IOException
    {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testEncStream is='{}'", text);
        oLogger.debug("size of input text is {}", testdata.length());
        final OutputStream os = new ByteArrayOutputStream();
        layer.encStream(is, os);
        os.flush();
        oLogger.debug("testEncStream os='{}'", os.toString());
        assertEquals("encoding differs", testdataEncoded, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#encStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-12")
    public void testEncStreamExceptionIn() throws JastacryException, IOException
    {
        final InputStream is = new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        final OutputStream os = new ByteArrayOutputStream();
        Assertions.assertThrows(JastacryException.class, () -> {
            layer.encStream(is, os);
        });
        os.flush();
        oLogger.debug("testEncStream os='{}'", os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#encStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException i case of error
     */
    @Test
    // TestLink(externalId = "JAS-12")
    public void testEncStreamExceptionOut() throws JastacryException, IOException
    {
        final byte[] buf = testdata.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testEncStream is='{}'", text);
        oLogger.debug("size of input text is {}", testdata.length());
        final OutputStream os = new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };

        Assertions.assertThrows(JastacryException.class, () -> {
            layer.encStream(is, os);
            os.flush();
            oLogger.debug("testEncStream os='{}'", os.toString());
        });
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-13")
    public void testDecStream() throws JastacryException, IOException
    {
        final byte[] buf = testdataEncoded.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testDecStream is='{}'", text);
        oLogger.debug("size of encoded text is {}", testdataEncoded.length());
        final OutputStream os = new ByteArrayOutputStream();
        layer.decStream(is, os);
        oLogger.debug("testDecStream os='{}'", os.toString());
        assertEquals("decoding differs", testdata, os.toString());
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-13")
    public void testDecStreamExceptionIn() throws JastacryException, IOException
    {
        final InputStream is = new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        final OutputStream os = new ByteArrayOutputStream();
        Assertions.assertThrows(JastacryException.class, () -> {
            layer.decStream(is, os);
        });
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     * @throws JastacryException
     *             in case of error.
     * @throws IOException in case of error
     */
    @Test
    // TestLink(externalId = "JAS-13")
    public void testDecStreamExceptionOut() throws JastacryException, IOException
    {
        final byte[] buf = testdataEncoded.getBytes();
        final InputStream is = new ByteArrayInputStream(buf);
        is.mark(0);
        final String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        is.reset();
        oLogger.debug("testDecStream is='{}'", text);
        oLogger.debug("size of encoded text is {}", testdataEncoded.length());
        final OutputStream os = new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        Assertions.assertThrows(JastacryException.class, () -> {
            layer.decStream(is, os);
        });
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#decStream(java.io.InputStream, java.io.OutputStream)} .
     *
     */
    @Test
    public void testRun()
    {
        final InputStream is = new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        is.mark(0);
        final OutputStream os = new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        layer.inputStream = is;
        layer.outputStream = os;
        layer.setEndController(endController);
        layer.setAction(Action.ENCODE);
        layer.run();
    }

    /**
     * Test method for {@link org.jastacry.layer.AsciiTransportLayer#toString()} .
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", AsciiTransportLayer.LAYERNAME, layer.toString());
    }

    /**
     * Testcase unsupported exception.
     * @throws JastacryException on error
     */
    @Test
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
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        AsciiTransportLayer l1 = new AsciiTransportLayer();
        AsciiTransportLayer l2 = new AsciiTransportLayer();
        assertEquals("Layer object equal", l1, l2);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        AsciiTransportLayer l1 = new AsciiTransportLayer();
        assertEquals("Layer object same", l1, l1);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        AsciiTransportLayer l1 = new AsciiTransportLayer();
        Object o = null;
        assertEquals("Layer object null unequal", l1.equals(o), false);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        AsciiTransportLayer l1 = new AsciiTransportLayer();
        Object o = new Object();
        assertEquals("Layer object wrong class unequal", l1.equals(o), false);
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        AsciiTransportLayer l1 = new AsciiTransportLayer();
        AsciiTransportLayer l2 = new AsciiTransportLayer();
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }
}
