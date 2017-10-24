package org.jastacry.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.layer.RandomLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLayerRandom {
    private final String testdata = "The quick brown fox jumps over the lazy dog.";
    private RandomLayer layer = null;

    @Before
    public void setUp() throws Exception {
        layer = new RandomLayer();
    }

    @After
    public void tearDown() throws Exception {
        layer = null;
    }

    @Test
    // TestLink(externalId = "JAS-6")
    public void testEncDecStream() throws IOException {
        byte[] buf = testdata.getBytes();
        final InputStream isEncode = new ByteArrayInputStream(buf);
        final ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
        layer.init("333");
        layer.encStream(isEncode, osEncode);
        buf = osEncode.toByteArray();

        layer = null;
        layer = new RandomLayer();
        final InputStream isDecode = new ByteArrayInputStream(buf);
        final OutputStream osDecode = new ByteArrayOutputStream();
        layer.init("333");
        layer.decStream(isDecode, osDecode);
        assertEquals("decoding differs", testdata, osDecode.toString());

    }

    @Test
    // TestLink(externalId = "JAS-7")
    public void testToString() {
        assertEquals("Layer name mismatch", RandomLayer.LAYERNAME, layer.toString());
    }

}
