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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.GlobalData.Action;
import org.jastacry.GlobalData.Returncode;
import org.jastacry.layer.AbstractBasicLayer;
import org.jastacry.layer.AesCbcLayer;
import org.jastacry.layer.AesCtrLayer;
import org.jastacry.layer.AesEcbLayer;
import org.jastacry.layer.AsciiTransportLayer;
import org.jastacry.layer.FilemergeLayer;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.layer.RandomLayer;
import org.jastacry.layer.ReadWriteLayer;
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
     * Minimal number of threads needed. Better use all cores.
     */
    private static final int MINUMUM_THREADS = 2;

    /**
     * Char to mark comments.
     */
    private static final char TOKEN_COMMENT = ';';

    /**
     * Token count comparator, means no argument to command.
     */
    private static final int TOKENCOUNT_ONE = 1;

    /**
     * boolean status: do we encode to text transport format?
     */
    private boolean doASCIItransport;

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
     * Thread pool object.
     */
    private final ThreadPoolExecutor executor;

    /**
     * Layer thread factory.
     */
    private final LayerThreadFactory threadFactory;


    /**
     * Constructor of Worker class.
     */
    public Worker() {
        LOGGER.traceEntry();
        final int numCores = Runtime.getRuntime().availableProcessors();
        LOGGER.trace("CPU cores available: {}", numCores);
        final int numThreads = Math.max(numCores, MINUMUM_THREADS);
        LOGGER.trace("Using {} threads in pool", numThreads);

        this.threadFactory = new LayerThreadFactory();
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        this.executor.setThreadFactory(threadFactory);
        LOGGER.traceExit();
    }

    /**
     * Main method for running a command line interface.
     *
     * @return int system return code to shell
     */
    public final int mainWork() {
        LOGGER.traceEntry();
        final List<AbstractBasicLayer> layers = createLayers();

        if (null == layers || layers.isEmpty()) {
            LOGGER.error("No layers defined!");
            return LOGGER.traceExit(Returncode.RC_ERROR.getNumVal());
        } // if

        if (layers.size() == 1) {
            LOGGER.warn("Warning: Only one layer defined!");
            return LOGGER.traceExit(Returncode.RC_ERROR.getNumVal());
        }

        // In case of plain text, either encode after layers or decode before them.
        if (doASCIItransport) {
            final AbstractBasicLayer layerEncode = new AsciiTransportLayer();
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
                    // will never be reached if main setup function works correctly
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
            return LOGGER.traceExit(Returncode.RC_ERROR.getNumVal());
        } catch (final IOException e) {
            LOGGER.catching(e);
        }

        if (isVerbose) {
            LOGGER.info("JaStaCry finished");
        }

        LOGGER.traceExit();
        return 0;
    }

    /**
     * Create Array of layer objects.
     *
     * @return List of abstract layer objects
     */
    @java.lang.SuppressWarnings("squid:S3776")
    private List<AbstractBasicLayer> createLayers() {
        LOGGER.traceEntry();
        final List<AbstractBasicLayer> layers = new ArrayList<>();

        // try with resources
        try (FileInputStream fstream = new FileInputStream(confFilename);
                InputStreamReader isr = new InputStreamReader(fstream, StandardCharsets.UTF_8);
                BufferedReader breader = new BufferedReader(isr)) {
            String strLine;

            AbstractBasicLayer layer = null;

            // Read File Line By Line
            while ((strLine = breader.readLine()) != null) {
                strLine = strLine.trim();
                if (TOKEN_COMMENT == strLine.charAt(0)) {
                    GlobalFunctions.logDebug(isVerbose, LOGGER, "skip comment line '{}'", strLine);
                } else {

                    String sLayer;
                    String sParams;

                    final String[] toks = strLine.split("\\s+");
                    // no parameter?
                    if (TOKENCOUNT_ONE == toks.length) {
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
                                return LOGGER.traceExit(layers);
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

        return LOGGER.traceExit(layers);
    }

    /**
     * Create layer objects by given String name.
     *
     * @param sName
     *            name of layer
     * @return Layer object
     */
    private AbstractBasicLayer createLayer(final String sName) {
        LOGGER.traceEntry(sName);
        AbstractBasicLayer layer;

        switch (sName.toLowerCase(Locale.getDefault())) {
            case GlobalData.LAYER_TRANSPARENT:
                layer = new TransparentLayer();
                break;
            case GlobalData.LAYER_XOR:
                layer = new XorLayer();
                break;
            case GlobalData.LAYER_ROTATE:
                layer = new RotateLayer();
                break;
            case GlobalData.LAYER_REVERSE:
                layer = new ReverseLayer();
                break;
            case GlobalData.LAYER_RANDOM:
                layer = new RandomLayer();
                break;
            case GlobalData.LAYER_FILEMERGE:
                layer = new FilemergeLayer();
                break;
            case GlobalData.LAYER_MD5DES:
                layer = new Md5DesLayer();
                break;
            case GlobalData.LAYER_AESCBC:
                layer = new AesCbcLayer();
                break;
            case GlobalData.LAYER_AESECB:
                layer = new AesEcbLayer();
                break;
            case GlobalData.LAYER_AESCTR:
                layer = new AesCtrLayer();
                break;
            default:
                LOGGER.error("unknown layer '{}'", sName);
                layer = null;
                break;
        } // switch

        return LOGGER.traceExit(layer);
    } // function

    /**
     * Loop through layers with data streams.
     *
     * @param layers Array of layers
     * @param input input Stream
     * @param output output Stream
     * @throws IOException in case of error
     */
    @java.lang.SuppressWarnings("squid:S2093")
    private void loopLayers(final List<AbstractBasicLayer> layers, final InputStream input, final OutputStream output) {
        LOGGER.traceEntry();

        final CountDownLatch endController = new CountDownLatch(layers.size() + 2);
        final List<AbstractBasicLayer> layersWithIO = new ArrayList<>();
        final List<InputStream> inputStreams = new ArrayList<>();
        final List<OutputStream> outputStreams = new ArrayList<>();

        AbstractBasicLayer l = null;
        PipedOutputStream prevOutput = null;
        PipedOutputStream pipedOutputFromFile = null;
        PipedInputStream pipedInputStream = null;
        PipedOutputStream pipedOutputStream = null;
        PipedInputStream pipedInputStreamToFile = null;

        try {
            // Handle file input
            pipedOutputFromFile = new PipedOutputStream();
            outputStreams.add(pipedOutputFromFile);
            l = new ReadWriteLayer();
            l.setInputStream(input);
            l.setOutputStream(pipedOutputFromFile);
            l.setAction(action);
            l.setEndController(endController);
            layersWithIO.add(l);

            // Handle very first layer
            l = layers.get(0);
            GlobalFunctions.logDebug(isVerbose, LOGGER, "layer FIRST '{}'", l);
            pipedInputStream = new PipedInputStream();
            pipedOutputStream = new PipedOutputStream();
            inputStreams.add(pipedInputStream);
            outputStreams.add(pipedOutputStream);
            pipedInputStream.connect(pipedOutputFromFile);
            prevOutput = pipedOutputStream;
            l.setInputStream(pipedInputStream);
            l.setOutputStream(pipedOutputStream);
            l.setAction(action);
            l.setEndController(endController);
            layersWithIO.add(l);

            // only second and further layers are looped through
            for (int i = 1; i < layers.size(); i++) {
                l = layers.get(i);

                GlobalFunctions.logDebug(isVerbose, LOGGER, "layer {} '{}'", i, l);

                pipedInputStream = new PipedInputStream(); // NOSONAR
                pipedOutputStream = new PipedOutputStream();
                inputStreams.add(pipedInputStream);
                outputStreams.add(pipedOutputStream);
                pipedInputStream.connect(prevOutput);
                prevOutput = pipedOutputStream;
                l.setInputStream(pipedInputStream);
                l.setOutputStream(pipedOutputStream);
                l.setAction(action);
                l.setEndController(endController);
                layersWithIO.add(l);
            } // for

            // Handle file output as very last layer
            pipedInputStreamToFile = new PipedInputStream(); // NOSONAR
            inputStreams.add(pipedInputStreamToFile);
            pipedInputStreamToFile.connect(prevOutput);
            l = new ReadWriteLayer();
            l.setInputStream(pipedInputStreamToFile);
            l.setOutputStream(output);
            l.setAction(action);
            l.setEndController(endController);
            layersWithIO.add(l);


            // Start all threads
            for (int i = 0; i < layersWithIO.size(); i++) {
                GlobalFunctions.logDebug(isVerbose, LOGGER, "start thread {}", i);
                final AbstractBasicLayer layer = layersWithIO.get(i);
                threadFactory.setNumber(i);
                executor.execute(layer);
            } // for

            // wait for all threads
            waitForThreads(endController);

        } catch (final IOException e) {
            LOGGER.catching(e);
        } finally {
            try {
                for (final InputStream inputStream : inputStreams) {
                    inputStream.close();
                } // for
                for (final OutputStream outputStream : outputStreams) {
                    outputStream.close();
                } // for
                inputStreams.clear();
                outputStreams.clear();
            } catch (final IOException e) {
                LOGGER.catching(e);
            }
        }

        LOGGER.traceExit();
    } // function

    /**
     * Wait for all threads to end.
     * @param endController the controller which counts the threads
     */
    private void waitForThreads(final CountDownLatch endController) {
        try {
            endController.await();
        } catch (final InterruptedException e) {
            LOGGER.catching(e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Destroy just like a inverted constructor function.
     */
    public final void destroy() {
        executor.shutdown();
    }

    /**
     * @param bStatus the doEncode to set
     */
    public final void setDoASCIItransport(final boolean bStatus) {
        doASCIItransport = bStatus;
    }

    /**
     * @param sFilename the confFilename to set
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
