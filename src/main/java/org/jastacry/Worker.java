package org.jastacry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.AbstractLayer;
import org.jastacry.layer.AesLayer;
import org.jastacry.layer.EncodeDecodeLayer;
import org.jastacry.layer.FilemergeLayer;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.layer.RandomLayer;
import org.jastacry.layer.RotateLayer;
import org.jastacry.layer.TransparentLayer;
import org.jastacry.layer.XorLayer;

/**
 * Real working class.
 *
 * @author kai
 *
 */
public class Worker {
    /**
     * log4j logger object.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * boolean status: do we encode?
     */
    private boolean doEncode;

    /**
     * Filename of configuration file.
     */
    private String confFilename;

    /**
     * Some input filename.
     */
    private String inputFilename;

    /**
     * Some output filename.
     */
    private String outputFilename;

    /**
     * Be verbose about every step?
     */
    private boolean isVerbose;

    /**
     * action variable.
     */
    private int action;

    /**
     * Main method for running a command line interface.
     *
     * @return int system return code to shell
     */
    public final int mainWork() {
        // Now go
        final List<AbstractLayer> layers = createLayers();

        if (null == layers || layers.isEmpty()) {
            LOGGER.error("No layers defined!");
            return org.jastacry.GlobalData.RC_ERROR;
        } // if

        if (layers.size() == 1) {
            LOGGER.warn("Warning: Only one layer defined!");
            return org.jastacry.GlobalData.RC_ERROR;
        }

        if (doEncode) {
            final AbstractLayer layerEncode = new EncodeDecodeLayer();
            switch (action) {
                case org.jastacry.GlobalData.ENCODE:
                    layers.add(layerEncode);
                    break;
                case org.jastacry.GlobalData.DECODE: // reverse order
                    layers.add(0, layerEncode);
                    break;
                default:
                    LOGGER.error("unknown action '{}'", action);
                    break;
            } // switch
        }

        final File fileIn = new File(inputFilename);
        final File fileOut = new File(outputFilename);

        try {
            try (InputStream input = new BufferedInputStream(new FileInputStream(fileIn));
                    OutputStream output = new BufferedOutputStream(new FileOutputStream(fileOut))) {
                loopLayers(layers, input, output);
            }
        } catch (final FileNotFoundException e) {
            LOGGER.catching(e);
            return org.jastacry.GlobalData.RC_ERROR;
        } catch (final IOException e) {
            LOGGER.catching(e);
        }

        if (isVerbose) {
            LOGGER.info("JaStaCry finished");
        }

        return 0;
    }

    /**
     * Create Array of layer objects.
     *
     * @return List of abstract layer objects
     */
    private List<AbstractLayer> createLayers() {
        final List<AbstractLayer> layers = new ArrayList<>();

        // try with resources
        try (FileInputStream fstream = new FileInputStream(confFilename);
                InputStreamReader isr = new InputStreamReader(fstream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {
            String strLine;

            AbstractLayer layer = null;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim();
                if (';' == strLine.charAt(0)) {
                    if (isVerbose) {
                        LOGGER.debug("skip comment line '{}'", strLine);
                    } // if
                } else {

                    String sLayer;
                    String sParams;

                    final String[] toks = strLine.split("\\s+");
                    // no parameter?
                    if (1 == toks.length) {
                        sLayer = strLine;
                        sParams = "";
                    } else {
                        sLayer = toks[0];
                        sParams = toks[1];
                        if (isVerbose && LOGGER.isDebugEnabled()) {
                            LOGGER.debug("read config, layer={}, params={}", sLayer, sParams);
                        } // if

                        // Optional interactive password entry
                        if (sParams.equalsIgnoreCase(org.jastacry.GlobalData.MACRO_PASSWORD)) {
                            final Console console = System.console();
                            if (null == console) {
                                LOGGER.error("No interactive console available for password entry!");
                                return layers;
                            }
                            final char[] password = console.readPassword("Layer " + sLayer + " Password: ");
                            sParams = new String(password);
                        }
                    }

                    switch (sLayer.toLowerCase(Locale.getDefault())) {
                        case "transparent":
                            layer = new TransparentLayer();
                            break;
                        case "xor":
                            layer = new XorLayer();
                            break;
                        case "rotate":
                            layer = new RotateLayer();
                            break;
                        case "random":
                            layer = new RandomLayer();
                            break;
                        case "filemerge":
                            layer = new FilemergeLayer();
                            break;
                        case "md5des":
                            layer = new Md5DesLayer();
                            break;
                        case "aes":
                            layer = new AesLayer();
                            break;
                        default:
                            LOGGER.error("unknown layer '{}'", sLayer);
                            continue;
                    } // switch

                    if (isVerbose && LOGGER.isDebugEnabled()) {
                        LOGGER.debug("adding layer {}", sLayer);
                    }

                    layer.init(sParams);
                    switch (action) {
                        case org.jastacry.GlobalData.ENCODE:
                            layers.add(layer);
                            break;
                        case org.jastacry.GlobalData.DECODE: // reverse order
                            layers.add(0, layer);
                            break;
                        default:
                            LOGGER.error("unkown action {}", action);
                            break;
                    } // switch
                } // if comment
            } // while

        } catch (final IOException e) {
            LOGGER.catching(e);
        }

        return layers;
    }

    /**
     * make nice name.
     *
     * @param iNumber
     *            of layer
     * @param layer
     *            itself
     * @return text name
     */
    private String makeThreadname(final int iNumber, final AbstractLayer layer) {
        return Integer.toString(iNumber) + " " + layer.toString();
    }

    /**
     * Loop through layers with data streams.
     *
     * @param layers
     *            Array of layers
     * @param input
     *            input Stream
     * @param output
     *            output Stream
     * @throws IOException
     *             in case of error
     */
    private void loopLayers(final List<AbstractLayer> layers, final InputStream input, final OutputStream output)
            throws IOException {
        AbstractLayer l = null;
        final List<AbstractThread> threads = new ArrayList<>();
        PipedOutputStream prevOutput = null;

        // Handle file input
        final PipedOutputStream pipedOutputFromFile = new PipedOutputStream();
        final ReaderThread readerThread = new ReaderThread(input, pipedOutputFromFile);
        threads.add(readerThread);

        // Handle very first layer
        l = layers.get(0);
        if (isVerbose && LOGGER.isDebugEnabled()) {
            LOGGER.debug("layer FIRST '{}'", l);
        } // if
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputFromFile);
        prevOutput = pipedOutputStream;
        LayerThread thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(0, l));
        threads.add(thread);

        // only inner layers are looped through
        for (int i = 1; i < layers.size() - 1; i++) {
            l = layers.get(i);

            if (isVerbose && LOGGER.isDebugEnabled()) {
                LOGGER.debug("layer {} '{}'", i, l);
            } // if

            pipedInputStream = new PipedInputStream();
            pipedOutputStream = new PipedOutputStream();
            pipedInputStream.connect(prevOutput);
            prevOutput = pipedOutputStream;
            thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(i, l));
            threads.add(thread);
        } // for

        // Handle last layer
        l = layers.get(layers.size() - 1);
        if (isVerbose && LOGGER.isDebugEnabled()) {
            LOGGER.debug("layer LAST '{}'", l);
        } // if
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(prevOutput);
        prevOutput = pipedOutputStream;
        thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(threads.size(), l));
        threads.add(thread);

        // Handle file output
        final PipedInputStream pipedInputStreamToFile = new PipedInputStream();
        pipedInputStreamToFile.connect(prevOutput);
        final WriterThread writerThread = new WriterThread(pipedInputStreamToFile, output);
        threads.add(writerThread);

        // Start all threads
        for (int i = 0; i < threads.size(); i++) {
            if (isVerbose && LOGGER.isDebugEnabled()) {
                LOGGER.debug("start thread {}", i);
            } // if
            threads.get(i).start();
        }
        // wait for all threads
        for (int i = 0; i < threads.size(); i++) {
            try {
                threads.get(i).join();
            } catch (final InterruptedException e) {
                LOGGER.catching(e);
                Thread.currentThread().interrupt();
            }
        }

    } // function

    /**
     * @param bStatus
     *            the doEncode to set
     */
    public final void setDoEncode(final boolean bStatus) {
        doEncode = bStatus;
    }

    /**
     * @param sFilename
     *            the confFilename to set
     */
    public final void setConfFilename(final String sFilename) {
        confFilename = sFilename;
    }

    /**
     * @param sFilename
     *            the inputFilename to set
     */
    public final void setInputFilename(final String sFilename) {
        inputFilename = sFilename;
    }

    /**
     * @param sFilename
     *            the outputFilename to set
     */
    public final void setOutputFilename(final String sFilename) {
        outputFilename = sFilename;
    }

    /**
     * @param bStatus
     *            the isVerbose to set
     */
    public final void setVerbose(final boolean bStatus) {
        isVerbose = bStatus;
    }

    /**
     * @param iAction
     *            the action to set
     */
    public final void setAction(final int iAction) {
        action = iAction;
    }

}
