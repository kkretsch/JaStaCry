package org.jastacry.layer;

import java.io.InputStream;
import java.io.OutputStream;

import org.jastacry.JastacryException;
import org.jastacry.GlobalData.Action;

/**
 * Layer interface for more separation of methods.
 * @author Kai Kretschmann
 *
 */
public interface Layer
{

    /**
     * Optional method for setting encryption or decryption
     * parameters like keys or passwords.
     *
     * @param data a String containing everything the layer needs to know
     */
    void init(String data);

    /**
     * Local encode Stream function which does the real thing for Random Layer.
     *
     * @param newInputStream incoming data
     * @param newOutputStream outgoing data
     * @throws JastacryException thrown on error
     */
    void encodeAndDecode(InputStream newInputStream, OutputStream newOutputStream)
            throws JastacryException;

    /**
     * Encodes either plain text or an encoded layer to the next encoding layer.
     *
     * @param newInputStream existing and opened input stream
     * @param newOutputStream existing and opened output stream
     * @throws JastacryException if one of the streams fail
     */
    default void encStream(InputStream newInputStream, OutputStream newOutputStream) throws JastacryException
    {
        encodeAndDecode(newInputStream, newOutputStream);
    }

    /**
     * Decodes an encrypted stream to either plain text or the next encoded layer.
     *
     * @param newInputStream existing and opened input stream
     * @param newOutputStream existing and opened output stream
     * @throws JastacryException if one of the streams fail
     */
    default void decStream(InputStream newInputStream, OutputStream newOutputStream) throws JastacryException
    {
        encodeAndDecode(newInputStream, newOutputStream);
    }

    /**
     * Property setter for input stream.
     *
     * @param newInputStream the new stream
     */
    void setInputStream(InputStream newInputStream);

    /**
     * property setter for output stream.
     *
     * @param newOutputStream the new output stream
     */
    void setOutputStream(OutputStream newOutputStream);

    /**
     * Property setter for action.
     *
     * @param newAction the new action
     */
    void setAction(Action newAction);

    /**
     * Property setter for realLayerName.
     *
     * @param newRealLayerName the new layer name
     */
    void setRealLayerName(String newRealLayerName);

}