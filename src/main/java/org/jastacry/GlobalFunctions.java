package org.jastacry;

import org.apache.logging.log4j.Logger;

/**
 * Global static functions are stored here.
 *
 * @author kai
 *
 */

public class GlobalFunctions {

    /**
     * Log debugging only if switched on via command line and log4j
     * 
     * @param isVerbose
     *            boolean
     * @param LOGGER
     *            log4j Logger object
     * @param sFormat
     *            Formatting string
     * @param arguments
     *            variable arguments
     */
    public static final void logDebug(final boolean isVerbose, final Logger LOGGER, final String sFormat,
            final Object... arguments) {
        if (isVerbose && LOGGER.isDebugEnabled()) {
            LOGGER.debug(sFormat, arguments);
        } // if
    } // function

} // class
