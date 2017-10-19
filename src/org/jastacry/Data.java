package org.jastacry;

public class Data {
	final public static String HELP = "JaStaCry [encode|decode] layers.conf infile outfile";
	
	final public static String TMPBASE = "jastacry";
	final public static String TMPEXT = ".tmp";

	final public static String PARAM_ENCODE = "encode";
	final public static String PARAM_DECODE = "decode";
	
	final public static int ENCODE = 1;
	final public static int DECODE = 2;

	final public static String MACRO_PASSWORD = "#PASSWORD#";

	final public static int RC_WARNING = 1;
	final public static int RC_HELP = 2;
	final public static int RC_ERROR = 3;
}
