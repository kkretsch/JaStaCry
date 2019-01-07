package org.jastacry;

import org.apache.logging.log4j.Logger;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Global static functions are stored here.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public final class GlobalFunctions
{

    /**
     * Log debugging only if switched on via command line and log4j.
     *
     * @param isVerbose boolean
     * @param logger log4j Logger object
     * @param sFormat Formatting string
     * @param arguments variable arguments
     */
    public static void logDebug(final boolean isVerbose, final Logger logger, final String sFormat, final Object... arguments)
    {
        if (isVerbose)
        {
            logger.debug(sFormat, () -> arguments);
        } // if
    } // function

    /**
     * Hidden constructor.
     */
    @CoverageIgnore
    private GlobalFunctions()
    {
        // not called
    }

} // class
