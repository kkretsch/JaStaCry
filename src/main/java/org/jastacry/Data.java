package org.jastacry;

/**
 * Class for constant values.
 *
 * @author Kai Kretschmann
 *
 */
public final class Data {
    /**
     * Hidden constructor.
     */
    private Data() {
        // not called
    }

    /**
     * Help line reply.
     */
    public static final String
        HELP = "JaStaCry -h | (-v) (-t) [--encode|--decode] --infile input.txt --outfile output.bin --conffile layers.conf";

    /**
     * Base name for temp files.
     */
    public static final String TMPBASE = "jastacry";
    /**
     * Extension for tempfiles.
     */
    public static final String TMPEXT = ".tmp";

    /**
     * Parameter for encoding.
     */
    public static final String PARAM_ENCODE = "encode";
    /**
     * Parameter for decoding.
     */
    public static final String PARAM_DECODE = "decode";

    /**
     * ID for encoding.
     */
    public static final int ENCODE = 1;
    /**
     * ID for decoding.
     */
    public static final int DECODE = 2;

    /**
     * Placholder for passwords.
     */
    public static final String MACRO_PASSWORD = "#PASSWORD#";

    /**
     * Returncode to shell for warnings.
     */
    public static final int RC_WARNING = 1;
    /**
     * Returncode to shell for help.
     */
    public static final int RC_HELP = 2;
    /**
     * Returncode to shell for errors.
     */
    public static final int RC_ERROR = 3;
}
