package org.jastacry.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.jastacry.JaStaCry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

/**
 * Test of Main function.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLocale
{
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testMainHelpEN()
    {
        final String[] sArguments = {
            "-h"
        };
        Locale englishLocale = new Locale("en", "GB");
        Locale.setDefault(englishLocale);
    
        JaStaCry.mainMethod(sArguments);
        String sOut = outContent.toString();
        boolean bFound = sOut.contains("show some help");
        assertTrue("Locale", bFound);
    }

    /**
     * Test method help for Main function.
     *
     */
    @Test
    public void testMainHelpDE()
    {
        final String[] sArguments = {
            "-h"
        };
        Locale germanLocale = new Locale("de", "DE");
        Locale.setDefault(germanLocale);

        JaStaCry.mainMethod(sArguments);
        String sOut = outContent.toString();
        boolean bFound = sOut.contains("zeige Hilfe an");
        assertTrue("Locale", bFound);
    }

}
