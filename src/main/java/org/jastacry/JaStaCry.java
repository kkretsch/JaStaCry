package org.jastacry;

import java.util.Locale;
import java.util.ResourceBundle;

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
import org.jastacry.GlobalData.Action;
import org.jastacry.GlobalData.Returncode;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Main JaStaCry class to start.
 *
 * <p>SPDX-License-Identifier: MIT
 *
 * @author Kai Kretschmann
 */
public final class JaStaCry
{
    /**
     * Holder of i18n translations.
     */
    private static ResourceBundle bundle;

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
     * Parameter, short version, for "ASCII".
     */
    private static final String P_SHORT_ASCII = "t";

    /**
     * Parameter, long version, for "ASCII".
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
    private static final Logger LOGGER = LogManager.getLogger(JaStaCry.class.getName());

    /**
     * boolean status: do we encode to plain text transport format.
     */
    private static boolean doASCIItransport;

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
     * Be verbose about every step.
     */
    private static boolean isVerbose;

    /**
     * action variable.
     */
    private static Action action;

    /**
     * Hidden constructor.
     */
    @CoverageIgnore
    private JaStaCry()
    {
        // not called
    }

    /**
     * Main class for running a command line interface.
     *
     * @param args parsed by Apache commons CLI package
     */
    @CoverageIgnore
    @SuppressWarnings("squid:S4823") // Using command line arguments is security-sensitive
    public static void main(final String[] args)
    {
        final int returncode = mainMethod(args);
        System.exit(returncode); // NOPMD by kai on 21.11.17 17:04
    }

    /**
     * Main method.
     *
     * @param args for parsing
     * @return int result code
     */
    public static int mainMethod(final String... args)
    {
        LOGGER.traceEntry();

        localizer();

        int returncode = setup(args);
        if (0 != returncode)
        {
            LOGGER.error(getText("error.setupfound"), returncode);
            return returncode; // NOPMD by kai on 21.11.17 16:59
        } // if

        final Worker worker = new Worker();
        worker.setAction(action);
        worker.setConfFilename(confFilename);
        worker.setDoAsciitransport(doASCIItransport);
        worker.setInputFilename(inputFilename);
        worker.setOutputFilename(outputFilename);
        worker.setVerbose(isVerbose);

        returncode = worker.mainWork();
        return LOGGER.traceExit(returncode);
    }

    /**
     * Create command line Options object.
     *
     * @return Options object
     */
    private static Options createOptions()
    {
        LOGGER.traceEntry();
        final Options options = new Options();

        // optional parameters
        options.addOption(P_SHORT_HELP, P_LONG_HELP, false, getText("help.help"));
        options.addOption(P_SHORT_VERBOSE, false, getText("help.verbose"));
        options.addOption(P_SHORT_ASCII, P_LONG_ASCII, false, getText("help.ascii"));

        // either/or arguments, but mandatory as a set
        final OptionGroup ogAction = new OptionGroup();
        Option option;
        option = Option.builder(P_SHORT_ENCODE).required(false).longOpt(P_LONG_ENCODE).desc(getText("help.encode")).build();
        ogAction.addOption(option);
        option = Option.builder(P_SHORT_DECODE).required(false).longOpt(P_LONG_DECODE).desc(getText("help.decode")).build();
        ogAction.addOption(option);
        ogAction.setRequired(true);
        options.addOptionGroup(ogAction);

        // mandatory parameters
        option = Option.builder(P_SHORT_CONFFILE).required(true).hasArg().longOpt(P_LONG_CONFFILE).argName("FILE")
                .desc("use FILE as stack configuration").build();
        options.addOption(option);

        option = Option.builder(P_SHORT_INFILE).required(true).hasArg().longOpt(P_LONG_INFILE).argName("FILE")
                .desc("use FILE as input stream").build();
        options.addOption(option);

        option = Option.builder(P_SHORT_OUTFILE).required(true).hasArg().longOpt(P_LONG_OUTFILE).argName("FILE")
                .desc("use FILE as output stream").build();
        options.addOption(option);

        return LOGGER.traceExit(options);
    }

    /**
     * Read current locale and load i18n bundle.
     */
    private static void localizer()
    {
        LOGGER.traceEntry();

        final Locale currentLocale = Locale.getDefault();
        LOGGER.info("Locale: {}", currentLocale);

        // First read locale i18n stuff
        bundle = ResourceBundle.getBundle("Bundle");

        LOGGER.traceExit();
    }

    /**
     * Helper function to get translated text from bundle.
     * @param key String key for value
     * @return String as translation
     */
    public static String getText(final String key)
    {
        return bundle.getString(key);
    }

    /**
     * Setup environment via command line arguments.
     *
     * @param args array of Strings from command line
     * @return int error value
     */
    private static int setup(final String... args)
    {
        LOGGER.traceEntry();

        // Command line parameters
        final Options options = createOptions();

        // Manual check for help, ignoring otherwise mandatory arguments
        final HelpFormatter formatter = new HelpFormatter();
        if (args.length > 0 && "-h".equalsIgnoreCase(args[0]))
        {
            formatter.printHelp(GlobalData.HELP, options);
            return Returncode.RC_HELP.getNumVal();
        } // if

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine;
        try
        {
            cmdLine = parser.parse(options, args);
        }
        catch (final MissingOptionException exOpt)
        {
            formatter.printHelp(GlobalData.HELP, options);
            return Returncode.RC_ERROR.getNumVal();
        }
        catch (final ParseException e2)
        {
            LOGGER.catching(e2);
            return LOGGER.traceExit(Returncode.RC_ERROR.getNumVal());
        }

        if (null == cmdLine)
        {
            LOGGER.error("cmdLine null");
            return LOGGER.traceExit(Returncode.RC_ERROR.getNumVal());
        }

        // No need to check for P_SHORT_HELP here anymore.

        // Is verbose?
        isVerbose = cmdLine.hasOption(P_SHORT_VERBOSE);
        if (isVerbose)
        {
            LOGGER.info("JaStaCry starting");
        }

        action = Action.UNKOWN;
        // is it called with all needed parameters?
        if (cmdLine.hasOption(P_SHORT_ENCODE) || cmdLine.hasOption(P_LONG_ENCODE))
        {
            action = Action.ENCODE;
        } // if
        if (cmdLine.hasOption(P_SHORT_DECODE) || cmdLine.hasOption(P_LONG_DECODE))
        {
            action = Action.DECODE;
        } // if

        // Use text format?
        doASCIItransport = cmdLine.hasOption(P_SHORT_ASCII) || cmdLine.hasOption(P_LONG_ASCII);

        // Get file names for config, input and output
        confFilename = cmdLine.getOptionValue(P_LONG_CONFFILE);
        inputFilename = cmdLine.getOptionValue(P_LONG_INFILE);
        outputFilename = cmdLine.getOptionValue(P_LONG_OUTFILE);

        return LOGGER.traceExit(0);
    }

}
