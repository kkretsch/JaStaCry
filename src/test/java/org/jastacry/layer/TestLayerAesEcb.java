package org.jastacry.layer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for AES Layer.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLayerAesEcb
{
    /**
     * Test data to play with.
     */
    private final String testdata = "The quick brown fox jumps over the lazy dog.";

    /**
     * Layer to test.
     */
    private AesEcbLayer layerEncrypt = null;

    /**
     * Layer to test.
     */
    private AesEcbLayer layerDecrypt = null;

    /**
     * Init value for random layer.
     */
    private static final String INITVALUE = "Passwort";

    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error.
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        layerEncrypt = new AesEcbLayer();
        layerEncrypt.init(INITVALUE);

        layerDecrypt = new AesEcbLayer();
        layerDecrypt.init(INITVALUE);
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case off error
     */
    @AfterEach
    public void tearDown() throws Exception
    {
        layerEncrypt = null;
        layerDecrypt = null;
    }

    /**
     * Testcase testEncDecStream.
     *
     * @throws JastacryException
     *             in case off error
     */
    @Test
    // TestLink(externalId = "JAS-4")
    public void testEncDecStream() throws JastacryException
    {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layerEncrypt.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layerDecrypt.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());

    }

    /**
     * Testcase testToString.
     */
    @Test
    public void testToString()
    {
        assertEquals("Layer name mismatch", AesEcbLayer.LAYERNAME, layerEncrypt.toString());
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testEquals()
    {
        AesEcbLayer l1 = new AesEcbLayer();
        AesEcbLayer l2 = new AesEcbLayer();
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
        AesEcbLayer l1 = new AesEcbLayer();
        l1.init(INITVALUE);
        assertEquals("Layer object same", l1, l1);
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsNull()
    {
        AesEcbLayer l1 = new AesEcbLayer();
        l1.init(INITVALUE);
        Object o = null;
        assertEquals("Layer object null unequal", false, l1.equals(o));
    }

    /**
     * Testcase equals.
     */
    @Test
    public void testNotEqualsWrongclass()
    {
        AesEcbLayer l1 = new AesEcbLayer();
        l1.init(INITVALUE);
        Object o = new Object();
        assertEquals("Layer object wrong class unequal", false, l1.equals(o));
    }

    /**
     * Testcase hashcode.
     */
    @Test
    public void testHashcode()
    {
        AesEcbLayer l1 = new AesEcbLayer();
        AesEcbLayer l2 = new AesEcbLayer();
        l1.init(INITVALUE);
        l2.init(INITVALUE);
        assertEquals("Layer hash equal", l1.hashCode(), l2.hashCode());
    }

}
