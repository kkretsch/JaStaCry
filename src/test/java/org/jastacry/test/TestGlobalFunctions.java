package org.jastacry.test;

import java.net.MalformedURLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jastacry.GlobalFunctions;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestGlobalFunctions
{
    /**
     * log4j2 object.
     */
    private static Logger oLogger = null;

    /**
     * Test input String.
     */
    public static final String MSG = "The quick fox ...";

    /**
     * The BeforeClass method.
     *
     * @throws MalformedURLException
     *             in case of error.
     */
    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        oLogger = LogManager.getLogger();
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogDebugTrue()
    {
        Configurator.setRootLevel(Level.DEBUG);
        GlobalFunctions.logDebug(true, oLogger, MSG);
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogDebugFalse()
    {
        Configurator.setRootLevel(Level.DEBUG);
        GlobalFunctions.logDebug(false, oLogger, MSG);
    }
    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogNoDebugTrue()
    {
        Configurator.setRootLevel(Level.INFO);
        GlobalFunctions.logDebug(true, oLogger, MSG);
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testLogNoDebugFalse()
    {
        Configurator.setRootLevel(Level.INFO);
        GlobalFunctions.logDebug(false, oLogger, MSG);
    }
}
