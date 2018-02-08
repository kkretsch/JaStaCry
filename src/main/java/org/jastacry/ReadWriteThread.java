/**
 *
 */
package org.jastacry;

import java.io.IOException;

/**
 * Own thread class.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
 */
public class ReadWriteThread extends AbstractThread {

    /**
     * main run method.
     */
    @Override
    public final void run() {
        LOGGER.info("started thread");
        try {
            int i = 0;
            while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }
            LOGGER.info("close stream");
            outputStream.close();
        } catch (final IOException e) {
            LOGGER.catching(e);
        }
        LOGGER.info("finished thread");
    }

}
