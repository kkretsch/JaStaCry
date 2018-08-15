package org.jastacry.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All test cases in one suite.
 *
 * @author Kai Kretschmann
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
    TestExportEncryption.class, TestConfig.class, TestEntropy.class, TestLayerAesCbc.class, TestLayerAesEcb.class,
    TestLayerAsciiTransport.class, TestLayerFilemerge.class, TestLayerMd5Des.class, TestLayerRandom.class, TestLayerReverse.class,
    TestLayerRotate.class, TestLayerTransparent.class, TestLayerXor.class, TestInputOutput.class, TestMain.class
})
public class AllTests
{

    /**
     * log4j logger object.
     */
    private static final Logger LOGGER = LogManager.getLogger(AllTests.class.getName());

    /**
     * All tests, initial test suite starting point.
     */
    public AllTests()
    {
        // empty constructor
        LOGGER.info("running AllTests");
    }
}
