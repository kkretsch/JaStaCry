package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.layer.XorLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLayerXor {
    private final String testdata = "The quick brown fox jumps over the lazy dog.";
    private XorLayer layer = null;

    @Before
    public void setUp() throws Exception {
        layer = new XorLayer();
    }

    @After
    public void tearDown() throws Exception {
        layer = null;
    }

    @Test
    // TestLink(externalId = "JAS-10")
    public void testEncDecStream() throws IOException {
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

    @Test
    // TestLink(externalId = "JAS-11")
    public void testToString() {
        assertEquals("Layer name mismatch", XorLayer.LAYERNAME, layer.toString());
    }

}
