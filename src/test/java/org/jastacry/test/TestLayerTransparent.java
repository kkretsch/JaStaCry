/**
 * 
 */
package org.jastacry.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.layer.TransparentLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kai Kretschmann
 *
 */
public class TestLayerTransparent {
	private String testdata = "The quick brown fox jumps over the lazy dog.";
	private TransparentLayer layer=null;

	@Before
	public void setUp() throws Exception {
		layer = new TransparentLayer();
	}

	@After
	public void tearDown() throws Exception {
		layer = null;
	}

	/**
	 * Test method for {@link org.jastacry.layer.TransparentLayer#encStream(java.io.InputStream, java.io.OutputStream)}.
	 * @throws IOException 
	 */
	@Test
	public void testEncStream() throws IOException {
		byte[] buf = testdata.getBytes();
		InputStream is = new ByteArrayInputStream(buf);
		OutputStream os = new ByteArrayOutputStream();
		layer.encStream(is, os);
		assertEquals("encoding differs", testdata, os.toString());
	}

	/**
	 * Test method for {@link org.jastacry.layer.TransparentLayer#decStream(java.io.InputStream, java.io.OutputStream)}.
	 * @throws IOException 
	 */
	@Test
	public void testDecStream() throws IOException {
		byte[] buf = testdata.getBytes();
		InputStream is = new ByteArrayInputStream(buf);
		OutputStream os = new ByteArrayOutputStream();
		layer.decStream(is, os);
		assertEquals("decoding differs", testdata, os.toString());
	}

	/**
	 * Test method for {@link org.jastacry.layer.TransparentLayer#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("Layer name mismatch", TransparentLayer.LAYERNAME, layer.toString());
	}

}
