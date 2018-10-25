package org.jastacry.layer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.jastacry.JastacryException;

/**
 * Mask every byte with some random data. The random stream is initialized by the init seed. Must be used the same on both sides
 * (encryption and decryption).
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public class RandomLayer extends AbstractBasicLayer
{
    /**
     * static name of the layer.
     */
    public static final String LAYERNAME = "Random Layer";

    /**
     * Random number generator.
     */
    private final java.util.Random rand = new java.util.Random();

    /**
     * Random number seed.
     */
    private long seed;

    /**
     * Constructor of RandomLayer.
     */
    public RandomLayer()
    {
        super(RandomLayer.class, LAYERNAME);
    }

    /**
     * Getter method.
     * @return long seed value
     */
    public final long getSeed()
    {
        return seed;
    }

    /**
     * Setter method.
     * @param newSeed long value
     */
    public final void setSeed(final long newSeed)
    {
        this.seed = newSeed;
    }

    /**
     * Init function.
     *
     * @param data to initialise the random seed value.
     */
    @Override
    public final void init(final String data)
    {
        setSeed(Long.parseLong(data));
        rand.setSeed(seed);
    }

    /**
     * Local encode Stream function which does the real thing for Random Layer.
     *
     * @param inputStream incoming data
     * @param outputStream outgoing data
     * @throws JastacryException thrown on error
     */
    public void encodeAndDecode(final InputStream inputStream, final OutputStream outputStream) throws JastacryException
    {
        try
        {
            int iChar;
            byte bChar;
            final byte[] bRand = new byte[1];
            while ((iChar = inputStream.read()) != -1)
            {
                bChar = (byte) iChar;
                this.rand.nextBytes(bRand);
                bChar = (byte) (bChar ^ bRand[0]);
                outputStream.write(bChar);
            }
            logger.info("close pipe");
            outputStream.close();
        }
        catch (IOException e)
        {
            throw (JastacryException) new JastacryException("encodeAndDecode failed").initCause(e);
        }
    }

    /**
     * Override equals method from object class.
     * @param o object to compare with
     * @return true or false
     */
    @Override
    public boolean equals(final Object o)
    {
        return o == this || o instanceof RandomLayer && ((RandomLayer) o).getSeed() == this.getSeed();
     }

    /**
     * Override equals method from object class.
     * @return hash of properties
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(seed);
    }
}
