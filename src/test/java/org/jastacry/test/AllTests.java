package org.jastacry.test;

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
@SuiteClasses({ TestExportEncryption.class,
    TestConfig.class, TestEntropy.class, TestLayerAes.class, TestLayerASCIITransport.class,
    TestLayerFilemerge.class, TestLayerMd5Des.class, TestLayerRandom.class, TestLayerReverse.class, TestLayerRotate.class, 
    TestLayerTransparent.class, TestLayerXor.class, TestIO.class, TestMain.class })
public class AllTests {

}
