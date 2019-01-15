package org.jastacry.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalFunctions;

import org.junit.jupiter.api.Test;

/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestGlobalFunctions
{

    /**
     * Test input String.
     */
    public static final String MSG = "The quick fox ...";

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogDebugTrue()
    {
        Logger oLogger = LogManager.getLogger();
        GlobalFunctions.logDebug(true, oLogger, MSG);
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogDebugFalse()
    {
        Logger oLogger = LogManager.getLogger();
        GlobalFunctions.logDebug(false, oLogger, MSG);
    }
}
