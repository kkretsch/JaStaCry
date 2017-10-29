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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jastacry.layer.AbsLayer;
import org.jastacry.layer.EncodeDecodeLayer;
import org.jastacry.layer.FilemergeLayer;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.layer.RandomLayer;
import org.jastacry.layer.RotateLayer;
import org.jastacry.layer.TransparentLayer;
import org.jastacry.layer.XorLayer;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Main JaStaCry class to start.
 *
 * @author Kai Kretschmann
 *
 */
public final class JaStaCry {
    /**
     * Parameter, short version, for "help".
     */
    private static final String P_SHORT_HELP = "h";

    /**
     * Parameter, long version, for "help".
     */
    private static final String P_LONG_HELP = "help";

    /**
     * Parameter, short version, for "verbose".
     */
    private static final String P_SHORT_VERBOSE = "v";

    /**
     * Parameter, short version, for "ascii".
     */
    private static final String P_SHORT_ASCII = "t";

    /**
     * Parameter, long version, for "ascii".
     */
    private static final String P_LONG_ASCII = "text";

    /**
     * Parameter, short version, for "encode".
     */
    private static final String P_SHORT_ENCODE = "e";

    /**
     * Parameter, long version, for "encode".
     */
    private static final String P_LONG_ENCODE = "encode";

    /**
     * Parameter, short version, for "decode".
     */
    private static final String P_SHORT_DECODE = "d";

    /**
     * Parameter, long version, for "decode".
     */
    private static final String P_LONG_DECODE = "decode";

    /**
     * Parameter, short version, for "config".
     */
    private static final String P_SHORT_CONFFILE = "c";

    /**
     * Parameter, long version, for "config".
     */
    private static final String P_LONG_CONFFILE = "conffile";

    /**
     * Parameter, short version, for "infile".
     */
    private static final String P_SHORT_INFILE = "i";

    /**
     * Parameter, long version, for "infile".
     */
    private static final String P_LONG_INFILE = "infile";

    /**
     * Parameter, short version, for "outfile".
     */
    private static final String P_SHORT_OUTFILE = "o";

    /**
     * Parameter, long version, for "outfile".
     */
    private static final String P_LONG_OUTFILE = "outfile";

    /**
     * log4j logger object.
     */
    private static Logger logger;

    /**
     * boolean status: do we encode?
     */
    private static boolean doEncode;

    /**
     * Filename of configuration file.
     */
    private static String confFilename;

    /**
     * Some input filename.
     */
    private static String inputFilename;

    /**
     * Some output filename.
     */
    private static String outputFilename;

    /**
     * Be verbose about every step?
     */
    private static boolean isVerbose;

    /**
     * action variable.
     */
    private static int action;

    /**
     * Hidden constructor.
     */
    @CoverageIgnore
    private JaStaCry() {
        // not called
    }

    /**
     * Main class for running a command line interface.
     *
     * @param args
     *            parsed by apache commons cli package
    */
    @CoverageIgnore
    public static void main(final String[] args) {
        int iRC = mainMethod(args);
        System.exit(iRC);
    }

    /**
     * Main method for running a command line interface.
     *
     * @param args
     *            parsed by apache commons cli package
     * @return int system return code to shell
     */
    public static int mainMethod(final String[] args) {
        logger = LogManager.getLogger();
        final int iRC = setup(args);
        if (0 != iRC) {
            logger.error("Setup found errors {}", iRC);
            return iRC;
        } // if

        // Now go
        final List<AbsLayer> layers = createLayers();

        if (0 == layers.size()) {
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
    private static void loopLayers(final List<AbsLayer> layers, final InputStream input, final OutputStream output,
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
     * Create command line Options object.
     *
     * @return Options object
     */
    private static Options createOptions() {
        Options options = new Options();
        Option option;

        // optional parameters
        options.addOption(P_SHORT_HELP, P_LONG_HELP, false, "show some help");
        options.addOption(P_SHORT_VERBOSE, false, "verbose");
        options.addOption(P_SHORT_ASCII, P_LONG_ASCII, false, "text formatted output or input of encrypted data");

        // either/or arguments
        OptionGroup ogAction = new OptionGroup();
        option = Option.builder(P_SHORT_ENCODE)
                .required(false)
                .longOpt(P_LONG_ENCODE)
                .desc("encode input stream")
                .build();
        ogAction.addOption(option);
        option = Option.builder(P_SHORT_DECODE)
                .required(false)
                .longOpt(P_LONG_DECODE)
                .desc("decode input stream")
                .build();
        ogAction.addOption(option);
        //ogAction.setRequired(true);
        options.addOptionGroup(ogAction);

        // potential mandatory parameters
        option = Option.builder(P_SHORT_CONFFILE)
                .required(false).hasArg()
                .longOpt(P_LONG_CONFFILE)
                .argName("FILE")
                .desc("use FILE as stack configuration")
                .build();
        options.addOption(option);

        option = Option.builder(P_SHORT_INFILE)
                .required(false).hasArg()
                .longOpt(P_LONG_INFILE)
                .argName("FILE")
                .desc("use FILE as input stream")
                .build();
        options.addOption(option);

        option = Option.builder(P_SHORT_OUTFILE)
                .required(false).hasArg()
                .longOpt(P_LONG_OUTFILE)
                .argName("FILE")
                .desc("use FILE as output stream")
                .build();
        options.addOption(option);

        return options;
    }


    /**
     * Setup environment via command line arguments.
     *
     * @param args
     *            array of Strings from command line
     * @return int error value
     */
    private static int setup(final String[] args) {
        // Command line parameters
        final Options options = createOptions();

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine = null;
        try {
            cmdLine = parser.parse(options, args);
        } catch (final MissingOptionException eOpt) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Data.HELP, options);
            return org.jastacry.Data.RC_ERROR;
        } catch (final ParseException e2) {
            logger.catching(e2);
            return org.jastacry.Data.RC_ERROR;
        }

        if (null == cmdLine) {
            logger.error("cmdLine null");
            return org.jastacry.Data.RC_ERROR;
        }

        if (cmdLine.hasOption(P_SHORT_HELP)) {
            logger.debug("Show help");
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Data.HELP, options);
            return org.jastacry.Data.RC_HELP;
        } // if

        // Is verbose?
        isVerbose = cmdLine.hasOption(P_SHORT_VERBOSE);
        if (isVerbose) {
            logger.info("JaStaCry starting");
        }

        action = 0;

        // is it called with all needed parameters?
        if (cmdLine.hasOption(P_SHORT_ENCODE) || cmdLine.hasOption(P_LONG_ENCODE)) {
            action = org.jastacry.Data.ENCODE;
        } // if
        if (cmdLine.hasOption(P_SHORT_DECODE) || cmdLine.hasOption(P_LONG_DECODE)) {
            action = org.jastacry.Data.DECODE;
        } // if
        if (0 == action) { // TODO should have been solved by commons-cli required attribute, which collides to help parameter
            logger.debug("action required");
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Data.HELP, options);
            return org.jastacry.Data.RC_ERROR;
        }

        // Use text format?
        doEncode = cmdLine.hasOption(P_SHORT_ASCII) || cmdLine.hasOption(P_LONG_ASCII);

        // Get file names for config, input and output
        confFilename = cmdLine.getOptionValue(P_LONG_CONFFILE);
        inputFilename = cmdLine.getOptionValue(P_LONG_INFILE);
        outputFilename = cmdLine.getOptionValue(P_LONG_OUTFILE);

        if (null == confFilename || null == inputFilename || null == outputFilename) {
            logger.debug("argument to parameter required");
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Data.HELP, options);
            return org.jastacry.Data.RC_ERROR;
        }

        return 0;
    }

    /**
     * Create Array of layer objects.
     *
     * @return List of abstract layer objects
     */
    private static List<AbsLayer> createLayers() {
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
                            System.exit(org.jastacry.Data.RC_ERROR);
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
}
