package org.jastacry.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import org.jastacry.layer.ReadWriteLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of IOException.
 *
 * @author Kai Kretschmann
 *
 */
public class TestInputOutput
{

    /**
     * The layer to test.
     */
    private ReadWriteLayer layer = null;

    /**
     * Needed for the thread countdown.
     */
    private CountDownLatch endController;

    /**
     * Test before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception
    {
        layer = new ReadWriteLayer();
        endController = new CountDownLatch(1);
        layer.setEndController(endController);
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
     * Testcase testIOException.
     *
     */
    @Test
    public void testIoException()
    {
        InputStream sInput;
        OutputStream sOutput;
        sInput = new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };
        sOutput = new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {
                throw new IOException("Expected as a test");
            }
        };

        layer.setInputStream(sInput);
        layer.setOutputStream(sOutput);
        layer.run();
    }

}
