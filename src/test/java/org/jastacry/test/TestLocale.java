package org.jastacry.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.jastacry.JaStaCry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of locale functions.
 *
 * @author Kai Kretschmann
 *
 */
public class TestLocale
{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /**
     * Test method help for Main function in english.
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
     * Test method help for Main function in german.
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
