package org.jastacry;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Class for constant values.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public final class GlobalData
{
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
     * Configuration value for layer "transparent" (lower case).
     */
    public static final String LAYER_TRANSPARENT = "transparent";

    /**
     * Configuration value for layer "xor" (lower case).
     */
    public static final String LAYER_XOR = "xor";

    /**
     * Configuration value for layer "rotate" (lower case).
     */
    public static final String LAYER_ROTATE = "rotate";

    /**
     * Configuration value for layer "reverse" (lower case).
     */
    public static final String LAYER_REVERSE = "reverse";

    /**
     * Configuration value for layer "random" (lower case).
     */
    public static final String LAYER_RANDOM = "random";

    /**
     * Configuration value for layer "filemerge" (lower case).
     */
    public static final String LAYER_FILEMERGE = "filemerge";

    /**
     * Configuration value for layer "md5des" (lower case).
     */
    public static final String LAYER_MD5DES = "md5des";

    /**
     * Configuration value for layer "aescbc" (lower case).
     */
    public static final String LAYER_AESCBC = "aescbc";

    /**
     * Configuration value for layer "aesecb" (lower case).
     */
    public static final String LAYER_AESECB = "aesecb";

    /**
     * Configuration value for layer "aesctr" (lower case).
     */
    public static final String LAYER_AESCTR = "aesctr";

    /**
     * enum range for Actions.
     *
     * @author Kai Kretschmann
     *
     */
    public enum Action
    {
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
     * enum range for return codes.
     *
     * @author Kai Kretschmann
     *
     */
    public enum Returncode
    {
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
         * @param iNumVal gives initial value
         */
        Returncode(final int iNumVal)
        {
            this.numVal = iNumVal;
        }

        /**
         * get numeric value of enum.
         *
         * @return integer value
         */
        public int getNumVal()
        {
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
    private GlobalData()
    {
        // not called
    }
}
