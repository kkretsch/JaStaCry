package org.jastacry;

import java.util.concurrent.ThreadFactory;

/**
 * Thread factory for better thread naming.
 * @author kai
 *
 */
public class LayerThreadFactory implements ThreadFactory {

    /**
     * Default thread name prefix.
     */
    private static final String PFX = "layer-";

    /**
     * No suffix needed.
     */
    private String suffix = "";

    @Override
    public final Thread newThread(final Runnable r) {
        final Thread thread = new Thread(r, PFX + suffix);
        thread.setDaemon(true);
        return thread;
    }

    /**
     * Set name suffix for thread.
     * @param sSuffix String as suffix.
     */
    public final void setName(final String sSuffix) {
        this.suffix = sSuffix;
    }

}
