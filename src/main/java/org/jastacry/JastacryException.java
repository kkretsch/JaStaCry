package org.jastacry;

public class JastacryException extends Exception
{

    /**
     * Calculated serial UUID.
     */
    private static final long serialVersionUID = 5001556363057011159L;

    public JastacryException(String msg)
    {
        super(msg);
    }
}
