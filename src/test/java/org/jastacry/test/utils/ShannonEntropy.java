package org.jastacry.test.utils;

import java.util.ArrayList;

/**
 * Helper functions for checking entropy.
 *
 */
public final class ShannonEntropy
{
    private double entropy = 0.0;

    public double getEntropy()
    {
        return entropy;
    }

    /**
     * Constructor of class.
     */
    public ShannonEntropy()
    {
        entropy = 0.0;
    }

    /**
     * Calculate entropy.
     * 
     * @param in
     *            String to analyze
     */
    public void calculate(final String in)
    {
        byte[] bArray = in.getBytes();
        calculate(bArray);
    } // function

    /**
     * Calculate entropy.
     * 
     * @param bArray
     *            byte array to analyze
     */
    public void calculate(final byte[] bArray)
    {
        entropy = 0.0;
        ArrayList<ByteFreq> freqs = new ArrayList<>();
        for (int i = 0; i < bArray.length; i++)
        {
            byte b = bArray[i];
            boolean flag = true;
            for (ByteFreq cf : freqs)
            {
                if (cf.byteValue == b)
                {
                    flag = false;
                    cf.count++;
                    break;
                } // if
                flag = true;
            } // for
            if (flag)
            {
                freqs.add(new ByteFreq(b));
            } // if
        } // for

        for (ByteFreq cf : freqs)
        {
            int freq = cf.count;
            if (freq == 0)
            {
                continue;
            } // if

            double c = (double) freq / bArray.length;
            entropy -= log2(Math.pow(c, c));
        } // for
    } // function

    private final double log2(double x)
    {
        return Math.log(x) / Math.log(2);
    } // math function

    @Override
    public String toString()
    {
        return "Entropy: " + this.entropy;
    } // function

    /**
     * inner class for array use.
     *
     */
    class ByteFreq
    {
        public final byte byteValue;
        public int count = 1;

        /**
         * inner constructor.
         * @param in byte to count
         */
        public ByteFreq(byte in)
        {
            this.byteValue = in;
        } // constructor

    } // inner class

} // class ShannonEntropy
