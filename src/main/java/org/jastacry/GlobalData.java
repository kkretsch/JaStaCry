package org.jastacry;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Class for constant values.
 *
 * @author Kai Kretschmann
 *
 */
public final class GlobalData {
    /**
     * Help line reply.
     */
    public static final String HELP = "JaStaCry -h | (-v) (-t) [--encode|--decode] --infile input.txt "
            + "--outfile output.bin --conffile layers.conf";

    /**
     * Base name for temporary files.
     */
    public static final String TMPBASE = "jastacry";

    /**
     * Extension for temporary files.
     */
    public static final String TMPEXT = ".tmp";

    /**
     * Extension for encrypted files.
     */
    public static final String ENCEXT = ".jac";

    /**
     * Parameter for encoding.
     */
    public static final String PARAM_ENCODE = "encode";

    /**
     * Parameter for decoding.
     */
    public static final String PARAM_DECODE = "decode";

    /**
     * enum range for Actions.
     *
     * @author Kai Kretschmann
     *
     */
    public enum Action {
        UNKOWN, ENCODE, DECODE;
    }

    /**
     * enum range for Returncodes.
     *
     * @author Kai Kretschmann
     *
     */
    public enum Returncode {
        RC_OK(0), RC_WARNING(1), RC_HELP(2), RC_ERROR(3);
        private int numVal;

        /**
         * Contructor of enum object.
         *
         * @param numVal
         *            gives initial value
         */
        Returncode(final int numVal) {
            this.numVal = numVal;
        }

        /**
         * get numeric value of enum.
         *
         * @return integer value
         */
        public int getNumVal() {
            return numVal;
        }
    }

    /**
     * Placeholder for passwords.
     */
    public static final String MACRO_PASSWORD = "#PASS" + "WORD#";

    /**
     * Hidden constructor.
     */
    @CoverageIgnore
    private GlobalData() {
        // not called
    }
}
