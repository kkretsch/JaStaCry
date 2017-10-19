package org.jastacry.test;

import static org.junit.Assert.assertEquals;

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
	private String testdata = "The quick brown fox jumps over the lazy dog.";
	private RandomLayer layer=null;

	@Before
	public void setUp() throws Exception {
		layer = new RandomLayer();
	}

	@After
	public void tearDown() throws Exception {
		layer = null;
	}

	@Test
	public void testEncDecStream() throws IOException {
		byte[] buf = testdata.getBytes();
		InputStream isEncode = new ByteArrayInputStream(buf);
		ByteArrayOutputStream osEncode = new ByteArrayOutputStream();
		layer.init("333");
		layer.encStream(isEncode, osEncode);
		buf = osEncode.toByteArray();

		layer = null;
		layer = new RandomLayer();
		InputStream isDecode = new ByteArrayInputStream(buf);
		OutputStream osDecode = new ByteArrayOutputStream();
		layer.init("333");
		layer.decStream(isDecode, osDecode);
		assertEquals("decoding differs", testdata, osDecode.toString());

	}


	@Test
	public void testToString() {
		assertEquals("Layer name mismatch", RandomLayer.LAYERNAME, layer.toString());
	}

}
