package org.jastacry;

import java.util.concurrent.ThreadFactory;

public class LayerThreadFactory implements ThreadFactory {

    private final static String PFX = "layer-";
    private String suffix="";

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, PFX+suffix);
        t.setDaemon(true);
        return t;
    }

    public void setName(final String sSuffix) {
        this.suffix = sSuffix;
    }
    
}
