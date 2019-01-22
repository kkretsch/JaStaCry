package org.jastacry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Real implementation of console.
 * @author Kai Kretschmann
 *
 */
public class RealConsoleService implements ConsoleServiceInterface
{
    /**
     * My real console object if any.
     */
    private final java.io.Console delegate;

    /**
     * log4j logger object.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /** Constructor.
     * 
     * @param delegate the real console object
     */
    public RealConsoleService(java.io.Console delegate) {
        this.delegate = delegate;
    }

    /**
     * Read password into char array.
     * @param prompt the prompt for your input
     * @return array of char
     */
    @Override
    public char[] readPassword(final String prompt)
    {
        if(null == delegate) {
            LOGGER.warn("delegate is null");
            return new char[] { 'X', 'X', 'X'};
        }
        return delegate.readPassword(prompt);
    }

}
