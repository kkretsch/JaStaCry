package org.jastacry.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalFunctions;

import static org.junit.Assert.assertEquals;

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
        Object o = null;
        assertEquals("Logger object null unequal", false, oLogger.equals(o));
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
        Object o = null;
        assertEquals("Logger object null unequal", false, oLogger.equals(o));
        GlobalFunctions.logDebug(false, oLogger, MSG);
    }
}
