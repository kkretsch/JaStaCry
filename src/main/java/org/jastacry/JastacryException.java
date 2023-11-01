package org.jastacry;

/**
 * My very own JaStaCry exception class.
 * @author kkretsch
 */
public class JastacryException extends Exception
{

    /**
     * Calculated serial UUID.
     */
    private static final long serialVersionUID = 5001556363057011159L;

    /**
     * Main constructor with string parameter.
     * @param msg The message
     */
    public JastacryException(final String msg)
    {
        super(msg);
    }
}
