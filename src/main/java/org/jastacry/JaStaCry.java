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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jastacry.layer.AbsLayer;
import org.jastacry.layer.EncodeDecodeLayer;
import org.jastacry.layer.FilemergeLayer;
import org.jastacry.layer.Md5DesLayer;
import org.jastacry.layer.RandomLayer;
import org.jastacry.layer.RotateLayer;
import org.jastacry.layer.TransparentLayer;
import org.jastacry.layer.XorLayer;

public class JaStaCry {
    static private final String P_SHORT_VERBOSE = "v";
    static private final String P_SHORT_ASCII = "t";
    static private final String P_LONG_ASCII = "text";
    static private final String P_SHORT_ENCODE = "e";
    static private final String P_LONG_ENCODE = "encode";
    static private final String P_SHORT_DECODE = "d";
    static private final String P_LONG_DECODE = "decode";
    static private final String P_LONG_CONFFILE = "conffile";
    static private final String P_LONG_INFILE = "infile";
    static private final String P_LONG_OUTFILE = "outfile";

    /**
     * Main class for running a command line interface
     *
     * @param args
     *            run as "[encode|decode] layers.conf infile outfile"
     */
    @SuppressWarnings("static-access")
    public static void main(final String[] args) {
        // Command line parameters
        final Options options = new Options();
        options.addOption(P_SHORT_ENCODE, P_LONG_ENCODE, false, "encode input stream");
        options.addOption(P_SHORT_DECODE, P_LONG_DECODE, false, "decode input stream");
        options.addOption(P_SHORT_VERBOSE, false, "verbose");
        options.addOption(P_SHORT_ASCII, P_LONG_ASCII, false, "text formatted output or input of encrypted data");
        options.addOption(OptionBuilder.withLongOpt(P_LONG_CONFFILE).withDescription("use FILE as stack configuration")
                .hasArg().withArgName("FILE").isRequired().create());
        options.addOption(OptionBuilder.withLongOpt(P_LONG_INFILE).withDescription("use FILE as input stream").hasArg()
                .withArgName("FILE").isRequired().create());
        options.addOption(OptionBuilder.withLongOpt(P_LONG_OUTFILE).withDescription("use FILE as output stream")
                .hasArg().withArgName("FILE").isRequired().create());

        final CommandLineParser parser = new PosixParser();
        CommandLine cmdLine = null;
        try {
            cmdLine = parser.parse(options, args);
        } catch (final MissingOptionException eOpt) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jastacry", options);
            System.exit(org.jastacry.Data.RC_ERROR);
        } catch (final ParseException e2) {
            e2.printStackTrace();
            System.exit(org.jastacry.Data.RC_ERROR);
        }

        if (null == cmdLine) {
            System.err.println("cmdLine null");
            return;
        }
        // Is verbose?
        final boolean isVerbose = cmdLine.hasOption(P_SHORT_VERBOSE);
        if (isVerbose) {
            System.out.println("JaStaCry starting");
        }

        int action = 0;

        // is it called with all needed parameters?
        if (cmdLine.hasOption(P_SHORT_ENCODE) || cmdLine.hasOption(P_LONG_ENCODE)) {
            action = org.jastacry.Data.ENCODE;
        } // if
        if (cmdLine.hasOption(P_SHORT_DECODE) || cmdLine.hasOption(P_LONG_DECODE)) {
            action = org.jastacry.Data.DECODE;
        } // if

        // Use text format?
        final boolean doEncode = cmdLine.hasOption(P_SHORT_ASCII) || cmdLine.hasOption(P_LONG_ASCII);

        // Get file names for config, input and output
        final String confFilename = cmdLine.getOptionValue(P_LONG_CONFFILE);
        final String inputFilename = cmdLine.getOptionValue(P_LONG_INFILE);
        final String outputFilename = cmdLine.getOptionValue(P_LONG_OUTFILE);

        // Now go
        final List<AbsLayer> layers = new ArrayList<AbsLayer>();

        FileInputStream fstream = null;
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(confFilename);
            br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            AbsLayer layer = null;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim();
                if (';' == strLine.charAt(0)) {
                    continue;
                }

                String sLayer = null;
                String sParams = "";

                final int iPosSpace = strLine.indexOf(' ');
                // no parameter?
                if (-1 == iPosSpace) {
                    sLayer = strLine;
                } else {
                    sLayer = strLine.substring(0, iPosSpace);
                    sParams = strLine.substring(iPosSpace + 1);

                    // System.out.println("Param '" + sParams + "'");

                    // Optional interactive password entry
                    if (sParams.equalsIgnoreCase(org.jastacry.Data.MACRO_PASSWORD)) {
                        final Console console = System.console();
                        if (null == console) {
                            System.err.println("No interactive console available for password entry!");
                            System.exit(org.jastacry.Data.RC_ERROR);
                            return;
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
                        System.err.println("unknown layer " + sLayer);
                        System.exit(org.jastacry.Data.RC_ERROR);
                        return;
                } // switch

                if (isVerbose) {
                    System.out.println("adding layer " + sLayer);
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
                        System.err.println("unkown action " + action);
                        break;
                } // switch
            }

        } catch (final FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        if (0 == layers.size()) {
            System.err.println("No layers defined!");
            System.exit(org.jastacry.Data.RC_ERROR);
            return;
        } // if

        if (1 == layers.size()) {
            System.out.println("Warning: Only one layer defined!");
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
                    System.err.println("unknown action " + action);
                    break;
            } // switch
        }

        AbsLayer l = null;

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
            e1.printStackTrace();
            System.exit(org.jastacry.Data.RC_ERROR);
        }

        try {
            input = new BufferedInputStream(new FileInputStream(fileIn));
            output = new BufferedOutputStream(new FileOutputStream(fileOut));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            System.exit(org.jastacry.Data.RC_ERROR);
        }

        try {
            for (int i = 0; i < layers.size(); i++) {
                l = layers.get(i);
                InputStream layerInput = null;
                OutputStream layerOutput = null;

                // first step
                if (i == 0) {
                    layerInput = input;
                    layerOutput = new BufferedOutputStream(new FileOutputStream(tempOut));
                    if (isVerbose) {
                        System.out.println("layer 0 '" + l.toString() + "' from " + fileIn + " to " + tempOut);
                    }
                } else {
                    // middle steps
                    if (i < layers.size() - 1) {
                        if (isVerbose) {
                            System.out.println(
                                    "layer " + i + " '" + l.toString() + "' from " + tempIn + " to " + tempOut);
                        }
                        layerInput = new BufferedInputStream(new FileInputStream(tempIn));
                        layerOutput = new BufferedOutputStream(new FileOutputStream(tempOut));
                    } else { // last step
                        if (isVerbose) {
                            System.out.println(
                                    "layer " + i + " '" + l.toString() + "' from " + tempIn + " to " + fileOut);
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
                        System.err.println("unknwon action " + action);
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
                    tempIn.delete();
                }
                tempIn = tempOut;
                tempOut = File.createTempFile(org.jastacry.Data.TMPBASE, org.jastacry.Data.TMPEXT);

            } // for

            if (null != tempIn) {
                tempIn.delete();
            }
            if (null != tempOut) {
                tempOut.delete();
            }

            if (null != input) {
                input.close();
            }
            if (null != output) {
                output.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (isVerbose) {
            System.out.println("JaStaCry finished");
        }
    }

}
