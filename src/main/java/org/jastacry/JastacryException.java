package org.jastacry;

/**
 * My very own JaStyCry exception class.
 * @author kkretsch
 */
public class JastacryException extends Exception
{

    /**
     * Calculated serial UUID.
     */
    private static final long serialVersionUID = 5001556363057011159L;

    public JastacryException(final String msg)
    {
        super(msg);
    }
}
