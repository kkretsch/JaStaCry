package org.jastacry.test;

import org.jastacry.JaStaCry;

public class Generator {

    private final static String RESOURCES = "src/test/resources/";

    public static void main(final String[] args) {
        final String sInputFile = RESOURCES + TestMain.INPUTFILE;
        final String sOutputFile = RESOURCES + TestMain.INPUTENCODED;
        final String sConfigFile = RESOURCES + TestMain.CONF1;

        final String[] sArguments = { "-v", "--encode", "--infile", sInputFile, "--outfile", sOutputFile, "--conffile",
                sConfigFile };
        JaStaCry.mainMethod(sArguments);
    }
}
