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
        /**
         * Unknown action.
         */
        UNKOWN,

        /**
         * Encode action.
         */
        ENCODE,

        /**
         * Decode action.
         */
        DECODE;
    }

    /**
     * enum range for Returncodes.
     *
     * @author Kai Kretschmann
     *
     */
    public enum Returncode {
        /**
         * Everything is OK.
         */
        RC_OK(0),

        /**
         * We had a warning.
         */
        RC_WARNING(1),

        /**
         * Showing help and quit.
         */
        RC_HELP(2),

        /**
         * Some major error happened.
         */
        RC_ERROR(3);

        /**
         * Private int value storage.
         */
        private int numVal;

        /**
         * Constructor of enum object.
         *
         * @param iNumVal
         *            gives initial value
         */
        Returncode(final int iNumVal) {
            this.numVal = iNumVal;
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
