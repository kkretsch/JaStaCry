package org.jastacry.test.utils;

import java.util.ArrayList;

/**
 * Helper functions for checking entropy.
 *
 */
public final class ShannonEntropy {
    private double entropy = 0.0;

    public double getEntropy() {
        return entropy;
    }

    /**
     * Constructor of class.
     * @param in String to check
     */
    public ShannonEntropy(String in) {
        ArrayList<charFreq> freqs = new ArrayList<>();
        String split[] = in.split("");
        for (String s : split) {
            boolean flag = true;
            for (charFreq cf : freqs) {
                if (cf.s.equals(s)) {
                    flag = false;
                    cf.count++;
                    break;
                } // if
                flag = true;
            } // for
            if (flag) {
                freqs.add(new charFreq(s));
            } // if
        } // for

        for (charFreq cf : freqs) {
            int freq = cf.count;
            if (freq == 0) {
                continue;
            } // if

            double c = (double) freq / in.length();
            entropy -= log2(Math.pow(c, c));
        } // for
    } // function constructor

    /**
     * Constructor of class.
     * @param bArray byte array to analyze
     */
    public ShannonEntropy(final byte[] bArray) {
        ArrayList<byteFreq> freqs = new ArrayList<>();
        for (int i = 0; i<bArray.length; i++) {
            byte b = bArray[i];
            boolean flag = true;
            for (byteFreq cf : freqs) {
                if (cf.b == b) {
                    flag = false;
                    cf.count++;
                    break;
                } // if
                flag = true;
            } // for
            if (flag) {
                freqs.add(new byteFreq(b));
            } // if
        } // for

        for (byteFreq cf : freqs) {
            int freq = cf.count;
            if (freq == 0) {
                continue;
            } // if

            double c = (double) freq / bArray.length;
            entropy -= log2(Math.pow(c, c));
        } // for
    } // function constructor

    private final double log2(double x) {
        return Math.log(x) / Math.log(2);
    } // math function

    @Override
    public String toString() {
        return "Entropy: " + this.entropy;
    } // function

    class charFreq {
        public final String s;
        public int count = 1;

        public charFreq(String in) {
            this.s = in;
        } // constructor

    } // inner class

    class byteFreq {
        public final byte b;
        public int count = 1;

        public byteFreq(byte in) {
            this.b = in;
        } // constructor

    } // inner class

} // class ShannonEntropy

