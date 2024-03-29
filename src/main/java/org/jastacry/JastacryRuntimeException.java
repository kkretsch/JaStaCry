package org.jastacry;

/**
 * My very own JaStaCry runtime exception class.
 * @author kkretsch
 */
public class JastacryRuntimeException extends RuntimeException
{

    /**
     * Calculated serial UUID.
     */
    private static final long serialVersionUID = 5001556363700511159L;

    /**
     * Main constructor with string parameter.
     * @param msg The message
     */
    public JastacryRuntimeException(final String msg)
    {
        super(msg);
    }
}

