package org.jastacry.layer;

import org.jastacry.GlobalData.Action;
import org.jastacry.JastacryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    private XorLayer layer = null; // NOPMD by kkretsch on 29.03.18 14:54

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
    @Before
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
    @After
    public void tearDown() throws Exception
    {
        layer = null;
    }

    /**
     * Testcase encode.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testActionEncode() throws JastacryException
    {
        layer.setAction(Action.ENCODE);
    }

    /**
     * Testcase decode.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testActionDecode() throws JastacryException
    {
        layer.setAction(Action.DECODE);
    }

    /**
     * Testcase unknown.
     *
     * @throws JastacryException
     *             in case of error
     */
    @Test
    public void testActionunknown() throws JastacryException
    {
        layer.setAction(Action.UNKOWN);
    }

}
