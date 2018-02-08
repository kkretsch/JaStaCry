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
import org.jastacry.GlobalData.Action;
import org.jastacry.GlobalData.Returncode;
import org.jastacry.layer.AbstractLayer;
import org.jastacry.layer.AesLayer;
import org.jastacry.layer.EncodeDecodeLayer;
import org.jastacry.layer.FilemergeLayer;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.layer.RandomLayer;
import org.jastacry.layer.ReverseLayer;
import org.jastacry.layer.RotateLayer;
import org.jastacry.layer.TransparentLayer;
import org.jastacry.layer.XorLayer;

/**
 * Real working class.
 *
 * SPDX-License-Identifier: MIT
 * @author Kai Kretschmann
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
    private Action action;

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
            return Returncode.RC_ERROR.getNumVal();
        } // if

        if (layers.size() == 1) {
            LOGGER.warn("Warning: Only one layer defined!");
            return Returncode.RC_ERROR.getNumVal();
        }

        if (doEncode) {
            final AbstractLayer layerEncode = new EncodeDecodeLayer();
            switch (action) {
                case ENCODE:
                    GlobalFunctions.logDebug(isVerbose, LOGGER, "add text encoding to end");
                    layers.add(layerEncode);
                    break;
                case DECODE: // reverse order
                    GlobalFunctions.logDebug(isVerbose, LOGGER, "add text encoding to beginning");
                    layers.add(0, layerEncode);
                    break;
                case UNKOWN:
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
            return Returncode.RC_ERROR.getNumVal();
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
    @java.lang.SuppressWarnings("squid:S3776")
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
                    GlobalFunctions.logDebug(isVerbose, LOGGER, "skip comment line '{}'", strLine);
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
                        GlobalFunctions.logDebug(isVerbose, LOGGER, "read config, layer={}, params={}", sLayer,
                                sParams);

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

                    layer = createLayer(sLayer);
                    if (null == layer) {
                        continue;
                    }
                    GlobalFunctions.logDebug(isVerbose, LOGGER, "adding layer {}", sLayer);

                    layer.init(sParams);
                    switch (action) {
                        case ENCODE:
                            layers.add(layer);
                            break;
                        case DECODE: // reverse order
                            layers.add(0, layer);
                            break;
                        case UNKOWN:
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
     * Create layer objects by given String name.
     *
     * @param sName
     *            name of layer
     * @return Layer object
     */
    private AbstractLayer createLayer(final String sName) {
        AbstractLayer layer = null;

        switch (sName.toLowerCase(Locale.getDefault())) {
            case "transparent":
                layer = new TransparentLayer();
                break;
            case "xor":
                layer = new XorLayer();
                break;
            case "rotate":
                layer = new RotateLayer();
                break;
            case "reverse":
                layer = new ReverseLayer();
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
                LOGGER.error("unknown layer '{}'", sName);
        } // switch

        return layer;
    } // function

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
    @java.lang.SuppressWarnings("squid:S2093")
    private void loopLayers(final List<AbstractLayer> layers, final InputStream input, final OutputStream output) {
        AbstractLayer l = null;
        final List<AbstractThread> threads = new ArrayList<>();
        PipedOutputStream prevOutput = null;
        PipedOutputStream pipedOutputFromFile = null;
        PipedInputStream pipedInputStream = null;
        PipedOutputStream pipedOutputStream = null;
        PipedInputStream pipedInputStreamToFile = null;

        try {
            // Handle file input
            pipedOutputFromFile = new PipedOutputStream();
            final ReaderThread readerThread = new ReaderThread(input, pipedOutputFromFile);
            threads.add(readerThread);

            // Handle very first layer
            l = layers.get(0);
            GlobalFunctions.logDebug(isVerbose, LOGGER, "layer FIRST '{}'", l);
            pipedInputStream = new PipedInputStream(); // NOSONAR
            pipedOutputStream = new PipedOutputStream(); // NOSONAR
            pipedInputStream.connect(pipedOutputFromFile);
            prevOutput = pipedOutputStream;
            LayerThread thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(0, l));
            threads.add(thread);

            // only inner layers are looped through
            for (int i = 1; i < layers.size() - 1; i++) {
                l = layers.get(i);

                GlobalFunctions.logDebug(isVerbose, LOGGER, "layer {} '{}'", i, l);

                pipedInputStream = new PipedInputStream();
                pipedOutputStream = new PipedOutputStream();
                pipedInputStream.connect(prevOutput);
                prevOutput = pipedOutputStream;
                thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(i, l));
                threads.add(thread);
            } // for

            // Handle last layer
            l = layers.get(layers.size() - 1);

            GlobalFunctions.logDebug(isVerbose, LOGGER, "layer LAST '{}'", l);

            pipedInputStream = new PipedInputStream();
            pipedOutputStream = new PipedOutputStream();
            pipedInputStream.connect(prevOutput);
            prevOutput = pipedOutputStream;
            thread = new LayerThread(pipedInputStream, pipedOutputStream, l, action, makeThreadname(threads.size(), l));
            threads.add(thread);

            // Handle file output
            pipedInputStreamToFile = new PipedInputStream();
            pipedInputStreamToFile.connect(prevOutput);
            final WriterThread writerThread = new WriterThread(pipedInputStreamToFile, output);
            threads.add(writerThread);

            // Start all threads
            for (int i = 0; i < threads.size(); i++) {
                GlobalFunctions.logDebug(isVerbose, LOGGER, "start thread {}", i);
                threads.get(i).start();
            }

            // wait for all threads
            waitThreads(threads);
        } catch (final IOException e) {
            LOGGER.catching(e);
        } finally {
            try {
                if (null != pipedOutputFromFile) {
                    pipedOutputFromFile.close();
                } // if
                if (null != pipedInputStream) {
                    pipedInputStream.close();
                } // if
                if (null != pipedOutputStream) {
                    pipedOutputStream.close();
                } // if
                if (null != pipedInputStreamToFile) {
                    pipedInputStreamToFile.close();
                } // if
            } catch (final IOException e) {
                LOGGER.catching(e);
            }
        }

    } // function

    /**
     * Wait for threads looping through them.
     *
     * @param threads
     *            a list of threads
     */
    private void waitThreads(final List<AbstractThread> threads) {
        for (int i = 0; i < threads.size(); i++) {
            try {
                threads.get(i).join();
            } catch (final InterruptedException e) {
                LOGGER.catching(e);
                Thread.currentThread().interrupt();
            }
        } // for

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
     * @param oAction
     *            the action to set
     */
    public final void setAction(final Action oAction) {
        this.action = oAction;
    }

}
