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
     * ID for encoding.
     */
    public static final int ENCODE = 1;

    /**
     * ID for decoding.
     */
    public static final int DECODE = 2;

    /**
     * Placeholder for passwords.
     */
    public static final String MACRO_PASSWORD = "#PASSWORD#";

    /**
     * Return code to shell for everything OK.
     */
    public static final int RC_OK = 0;

    /**
     * Return code to shell for warnings.
     */
    public static final int RC_WARNING = 1;

    /**
     * Return code to shell for help.
     */
    public static final int RC_HELP = 2;

    /**
     * Return code to shell for errors.
     */
    public static final int RC_ERROR = 3;

    /**
     * Hidden constructor.
     */
    @CoverageIgnore
    private GlobalData() {
        // not called
    }
}
