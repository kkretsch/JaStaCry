package org.jastacry.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

import org.jastacry.layer.ReadWriteLayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    @BeforeEach
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
    @AfterEach
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

        Object o = null;
        assertEquals("Layer object null unequal", false, layer.equals(o));

        layer.setInputStream(sInput);
        layer.setOutputStream(sOutput);
//        Assertions.assertThrows(IOException.class, () -> {
            layer.run();
//        });
    }

}
