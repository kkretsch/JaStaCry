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
 * Test of Layer Random.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerRandom
{
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * The layer to test.
     */
    private RandomLayer layer = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "333";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        layer = new RandomLayer();
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
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new RandomLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init(INITVALUE);
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());

    }

    /**
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", RandomLayer.LAYERNAME, layer.toString());
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
        RandomLayer l1 = new RandomLayer();
        RandomLayer l2 = new RandomLayer();
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
        RandomLayer l1 = new RandomLayer();
        RandomLayer l2 = new RandomLayer();
        l1.init(INITVALUE);
        l2.init("44");
        assertEquals("Layer object not equal", false, l1.equals(l2));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEqualsSame()
    {
        RandomLayer l1 = new RandomLayer();
        l1.init(INITVALUE);
        assertEquals("Layer object same", true, l1.equals(l1));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        RandomLayer l1 = new RandomLayer();
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
        RandomLayer l1 = new RandomLayer();
        Object o = new Object();
        l1.init(INITVALUE);
        assertEquals("Layer object type unequal", false, l1.equals(o));
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        RandomLayer l1 = new RandomLayer();
        RandomLayer l2 = new RandomLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
