package org.jastacry;

import java.util.concurrent.ThreadFactory;

/**
 * Thread factory for better thread naming.
 * @author kai
 *
 */
public class LayerThreadFactory implements ThreadFactory {

    private static final String PFX = "layer-";
    private String suffix="";

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, PFX+suffix);
        t.setDaemon(true);
        return t;
    }

    /**
     * Set name suffix for thread.
     * @param sSuffix String as suffix.
     */
    public void setName(final String sSuffix) {
        this.suffix = sSuffix;
    }
    
}
