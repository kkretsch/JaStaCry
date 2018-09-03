package org.jastacry.test.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.JastacryException;
import org.jastacry.layer.Layer;

/**
 * Helper functions for automated tests.
 * 
 * @author kai
 *
 */
public final class Tooling
{ // NOPMD by kai on 21.11.17 16:53
    /**
     * log4j2 object.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * How big can a byte be.
     */
    private static final int MAXBYTE = 255;

    /**
     * compare two files if they are equal.
     *
     * @param file1
     *            file one
     * @param file2
     *            file two
     * @return true if equal
     */
    public boolean compareFiles(final File file1, final File file2)
    {
        boolean bResult = false; // NOPMD by kai on 21.11.17 16:54
        try
        {
            bResult = FileUtils.contentEquals(file1, file2);
        }
        catch (IOException e)
        {
            LOGGER.catching(e);
        }
        return bResult;
    }

    /**
     * List all installed crypto providers.
     */
    public void listProviders()
    {
        final Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++)
        {
            LOGGER.info("Provider #{} {}", i, providers[i]);
            for (final Enumeration<Object> e = providers[i].keys(); e.hasMoreElements();)
            {
                LOGGER.info("\t{}", e.nextElement());
            }
        }
    }

    /**
     * Create a binary input file with all byte values possible.
     *
     * @param file
     *            File to create
     */
    public void createBinaryTestfile(final File file)
    {
        try
        {
            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file)))
            {
                for (int i = 0; i <= MAXBYTE; i++)
                {
                    outputStream.writeByte(i);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.catching(e);
        }
        catch (IOException e)
        {
            LOGGER.catching(e);
        }
    }

    /**
     * Create a binary input file with only one byte values used.
     *
     * @param file
     *            File to create
     * @param length
     *            how many bytes to write
     * @param bValue
     *            the byte to write
     */
    public void createBinaryTestfile(final File file, final long length, final byte bValue)
    {
        try
        {
            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file)))
            {
                for (int i = 0; i <= length; i++)
                {
                    outputStream.writeByte(bValue);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.catching(e);
        }
        catch (IOException e)
        {
            LOGGER.catching(e);
        }
    }

    /**
     * General mockup for IO Exceptions.
     * @param layer The layer to be tested
     * @throws JastacryException on error
     * @throws IOException on error
     */
    public void mockupInputOutputEncStreams(Layer layer) throws  JastacryException, IOException
    {
        InputStream in = org.mockito.Mockito.mock(InputStream.class);
        OutputStream out = org.mockito.Mockito.mock(OutputStream.class);
        org.mockito.Mockito.when(in.read()).thenThrow(new IOException());
        //org.mockito.Mockito.when(out.write(0)).thenThrow(new IOException());
        layer.init("1");
        layer.encStream(in, out);
    }

    /**
     * General mockup for IO Exceptions.
     * @param layer The layer to be tested
     * @throws JastacryException on error
     * @throws IOException on error
     */
    public void mockupInputOutputDecStreams(Layer layer) throws  JastacryException, IOException
    {
        InputStream in = org.mockito.Mockito.mock(InputStream.class);
        OutputStream out = org.mockito.Mockito.mock(OutputStream.class);
        org.mockito.Mockito.when(in.read()).thenThrow(new IOException());
        //org.mockito.Mockito.when(out.write(0)).thenThrow(new IOException());
        layer.init("1");
        layer.decStream(in, out);
    }
}
