package org.jastacry;

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
     * Main method.
     * @param args for parsing
     * @return int result code
     */
    public static int mainMethod(final String[] args) {
        logger = LogManager.getLogger();
        int iRC = setup(args);
        if (0 != iRC) {
            logger.error("Setup found errors {}", iRC);
            return iRC;
        } // if

        Worker worker = new Worker();
        worker.setAction(action);
        worker.setConfFilename(confFilename);
        worker.setDoEncode(doEncode);
        worker.setInputFilename(inputFilename);
        worker.setOutputFilename(outputFilename);
        worker.setVerbose(isVerbose);

        iRC = worker.mainWork();
        return iRC;
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

}
