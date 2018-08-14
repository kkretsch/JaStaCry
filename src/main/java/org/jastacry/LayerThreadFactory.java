package org.jastacry;

import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread factory for better thread naming.
 *
 * @author kai
 *
 */
public class LayerThreadFactory implements ThreadFactory
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
        final Thread thread = new Thread(r, PFX + suffix);
        thread.setDaemon(true);
        return LOGGER.traceExit(thread);
    }

    /**
     * Set name suffix for thread.
     *
     * @param sSuffix
     *            String as suffix.
     */
    public final void setName(final String sSuffix)
    {
        this.suffix = sSuffix;
    }

    /**
     * Set name suffix for thread.
     *
     * @param iSuffix
     *            Number as suffix.
     */
    public final void setNumber(final int iSuffix)
    {
        this.suffix = Integer.toString(iSuffix);
    }

}
