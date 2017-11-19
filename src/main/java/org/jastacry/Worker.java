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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.AbsLayer;
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
 * @author kai
 *
 */
public class Worker {
    /**
     * log4j logger object.
     */
    private Logger logger;
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

     * @return int system return code to shell
     */
    public final int mainWork() {
        logger = LogManager.getLogger();

        // Now go
        final List<AbsLayer> layers = createLayers();

        if (null == layers || 0 == layers.size()) {
            logger.error("No layers defined!");
            return org.jastacry.Data.RC_ERROR;
        } // if

        if (1 == layers.size()) {
            logger.warn("Warning: Only one layer defined!");
            return org.jastacry.Data.RC_ERROR;
        }

        if (doEncode) {
            final AbsLayer layerEncode = new EncodeDecodeLayer();
            switch (action) {
                case org.jastacry.Data.ENCODE:
                    layers.add(layerEncode);
                    break;
                case org.jastacry.Data.DECODE: // reverse order
                    layers.add(0, layerEncode);
                    break;
                default:
                    logger.error("unknown action '{}'", action);
                    break;
            } // switch
        }

        InputStream input = null;
        final File fileIn = new File(inputFilename);

        OutputStream output = null;
        final File fileOut = new File(outputFilename);

        // two temporary files for now
        File tempIn = null;
        File tempOut = null;
        try {
            tempIn = File.createTempFile(org.jastacry.Data.TMPBASE, org.jastacry.Data.TMPEXT);
            tempOut = File.createTempFile(org.jastacry.Data.TMPBASE, org.jastacry.Data.TMPEXT);
        } catch (final IOException e1) {
            logger.catching(e1);
            return org.jastacry.Data.RC_ERROR;
        }

        try {
            input = new BufferedInputStream(new FileInputStream(fileIn));
            output = new BufferedOutputStream(new FileOutputStream(fileOut));
        } catch (final FileNotFoundException e) {
            logger.catching(e);
            return org.jastacry.Data.RC_ERROR;
        }

        try {
            loopLayers(layers, input, output, tempIn, tempOut, fileIn, fileOut);
        } catch (final IOException e) {
            logger.catching(e);
        }

        if (isVerbose) {
            logger.info("JaStaCry finished");
        }

        return 0;
    }

    /**
     * Create Array of layer objects.
     *
     * @return List of abstract layer objects
     */
    private List<AbsLayer> createLayers() {
        final List<AbsLayer> layers = new ArrayList<AbsLayer>();

        // try with resources
        try (FileInputStream fstream = new FileInputStream(confFilename);
                InputStreamReader isr = new InputStreamReader(fstream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {
            String strLine;

            AbsLayer layer = null;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim();
                if (';' == strLine.charAt(0)) {
                    if (isVerbose) {
                        logger.debug("skip comment line '{}'", strLine);
                    } // if
                    continue;
                }

                String sLayer = null;
                String sParams = "";

                String[] toks = strLine.split("\\s+");
                // no parameter?
                if (1 == toks.length) {
                    sLayer = strLine;
                } else {
                    sLayer = toks[0];
                    sParams = toks[1];
                    if (isVerbose) {
                        logger.debug("read config, layer={}, params={}", sLayer, sParams);
                    } // if

                    // Optional interactive password entry
                    if (sParams.equalsIgnoreCase(org.jastacry.Data.MACRO_PASSWORD)) {
                        final Console console = System.console();
                        if (null == console) {
                            logger.error("No interactive console available for password entry!");
                            return null;
                        }
                        final char[] password = console.readPassword("Layer " + sLayer + " Password: ");
                        sParams = new String(password);
                    }
                }

                switch (sLayer.toLowerCase()) {
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
                        logger.error("unknown layer '{}'", sLayer);
                        continue;
                } // switch

                if (isVerbose) {
                    logger.debug("adding layer {}", sLayer);
                }

                layer.init(sParams);
                switch (action) {
                    case org.jastacry.Data.ENCODE:
                        layers.add(layer);
                        break;
                    case org.jastacry.Data.DECODE: // reverse order
                        layers.add(0, layer);
                        break;
                    default:
                        logger.error("unkown action {}", action);
                        break;
                } // switch
            }

        } catch (final FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return layers;
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
     * @param tempInArg
     *            temp file ingoing
     * @param tempOutArg
     *            temp file outgoing
     * @param fileIn
     *            input file
     * @param fileOut
     *            output file
     * @throws IOException
     *             in case of error
     */
    private void loopLayers(final List<AbsLayer> layers, final InputStream input, final OutputStream output,
            final File tempInArg, final File tempOutArg, final File fileIn, final File fileOut) throws IOException {
        File tempIn = tempInArg;
        File tempOut = tempOutArg;
        AbsLayer l = null;

        for (int i = 0; i < layers.size(); i++) {
            l = layers.get(i);
            InputStream layerInput = null;
            OutputStream layerOutput = null;

            // first step
            if (i == 0) {
                layerInput = input;
                layerOutput = new BufferedOutputStream(new FileOutputStream(tempOut));
                if (isVerbose) {
                    logger.debug("layer 0 '" + l.toString() + "' from " + fileIn + " to " + tempOut);
                }
            } else {
                // middle steps
                if (i < layers.size() - 1) {
                    if (isVerbose) {
                        logger.debug("layer " + i + " '" + l.toString() + "' from " + tempIn + " to " + tempOut);
                    }
                    layerInput = new BufferedInputStream(new FileInputStream(tempIn));
                    layerOutput = new BufferedOutputStream(new FileOutputStream(tempOut));
                } else { // last step
                    if (isVerbose) {
                        logger.debug("layer " + i + " '" + l.toString() + "' from " + tempIn + " to " + fileOut);
                    }
                    layerInput = new BufferedInputStream(new FileInputStream(tempIn));
                    layerOutput = output;
                }
            }

            switch (action) {
                case org.jastacry.Data.ENCODE:
                    l.encStream(layerInput, layerOutput);
                    break;
                case org.jastacry.Data.DECODE:
                    l.decStream(layerInput, layerOutput);
                    break;
                default:
                    logger.error("unknwon action '{}'", action);
                    break;
            }

            // first step
            if (i == 0) {
                layerOutput.close();
            } else {
                // middle steps
                if (i < layers.size() - 1) {
                    layerInput.close();
                    layerOutput.close();
                } else { // last step
                    layerInput.close();
                }
            }

            // transfer last temporary out to new temporary in
            if (null != tempIn) {
                final boolean bRC = tempIn.delete();
                if (!bRC) {
                    logger.warn("delete might have failed");
                }
            }
            tempIn = tempOut;
            tempOut = File.createTempFile(org.jastacry.Data.TMPBASE, org.jastacry.Data.TMPEXT);

        } // for

        if (null != tempIn) {
            final boolean bRC = tempIn.delete();
            if (!bRC) {
                logger.warn("delete might have failed");
            }
        }
        if (null != tempOut) {
            final boolean bRC = tempOut.delete();
            if (!bRC) {
                logger.warn("delete might have failed");
            }
        }

        if (null != input) {
            input.close();
        }
        if (null != output) {
            output.close();
        }
    }

    /**
     * @return the doEncode
     */
    public final boolean isDoEncode() {
        return doEncode;
    }

    /**
     * @param b the doEncode to set
     */
    public final void setDoEncode(final boolean b) {
        doEncode = b;
    }

    /**
     * @return the confFilename
     */
    public final String getConfFilename() {
        return confFilename;
    }

    /**
     * @param s the confFilename to set
     */
    public final void setConfFilename(final String s) {
        confFilename = s;
    }

    /**
     * @return the inputFilename
     */
    public final String getInputFilename() {
        return inputFilename;
    }

    /**
     * @param s the inputFilename to set
     */
    public final void setInputFilename(final String s) {
        inputFilename = s;
    }

    /**
     * @return the outputFilename
     */
    public final String getOutputFilename() {
        return outputFilename;
    }

    /**
     * @param s the outputFilename to set
     */
    public final void setOutputFilename(final String s) {
        outputFilename = s;
    }

    /**
     * @return the isVerbose
     */
    public final boolean isVerbose() {
        return isVerbose;
    }

    /**
     * @param b the isVerbose to set
     */
    public final void setVerbose(final boolean b) {
        isVerbose = b;
    }

    /**
     * @return the action
     */
    public final int getAction() {
        return action;
    }

    /**
     * @param i the action to set
     */
    public final void setAction(final int i) {
        action = i;
    }

}
