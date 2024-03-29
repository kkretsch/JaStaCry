package org.jastacry;

import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread factory for better thread naming.
 *
 * <p>SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 *
 */
class LayerThreadFactory implements ThreadFactory
{

    /**
     * log4j logger object.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Default thread name prefix.
     */
    private static final String PFX = "layer-";

    /**
     * No suffix needed.
     */
    private String suffix = "";

    @Override
    public final Thread newThread(final Runnable r)
    {
        LOGGER.traceEntry();
        final var thread = new Thread(r, PFX + suffix);
        thread.setDaemon(true);
        return LOGGER.traceExit(thread);
    }

    /**
     * Set name suffix for thread.
     *
     * @param iSuffix Number as suffix.
     */
    public final void setNumber(final int iSuffix)
    {
        this.suffix = Integer.toString(iSuffix);
    }

}
