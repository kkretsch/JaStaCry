/**
 *
 */
package org.jastacry.test;

import static org.junit.Assert.*;

import org.jastacry.JaStaCry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestMain {
    /**
     * Test Before method.
     *
     * @throws Exception
     *             in case of error
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test After method.
     *
     * @throws Exception
     *             in case of error
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for Main function.
     *
     */
    @Test
    public void testMainHelp() {
        final String[] sArguments = {"-h"};
        final int iRC = JaStaCry.main(sArguments);
        assertEquals("Main help returncode", iRC, org.jastacry.Data.RC_HELP);
    }
}
