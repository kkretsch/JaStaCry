package org.jastacry.layer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

import org.jastacry.GlobalData.Action;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of Layer XOR.
 *
 * @author Kai Kretschmann
 *
 */
public class TestAbstractBasicLayer
{
    /**
     * The layer to test.
     */
    private XorLayer layer = null;
    private CountDownLatch endController = new CountDownLatch(1);
    private byte[] data = {1,2,3};
    
    /**
     * Init value for xor layer.
     */
    private static final String INITVALUE = "123";

    /**
     * Test before method.
     *
     * @throws Exception
     *             in case of error
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        layer = new XorLayer();
        layer.init(INITVALUE);
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
     * Testcase unknown.
     *
     */
    @Test
    public void testActionunknown()
    {
        InputStream sInput;
        OutputStream sOutput;
        sInput = new ByteArrayInputStream(data);
        sOutput = new ByteArrayOutputStream();

        Object o = null;
        assertEquals("Layer object null unequal", false, layer.equals(o));

        layer.setInputStream(sInput);
        layer.setOutputStream(sOutput);
        layer.setAction(Action.UNKOWN);
        layer.setEndController(endController);

        layer.run();
    }

}
