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
@SuiteClasses({ TestLayerMd5Des.class, TestLayerRandom.class, TestLayerRotate.class, TestLayerReverse.class,
        TestLayerTransparent.class, TestLayerXor.class, TestLayerFilemerge.class, TestMain.class, TestEntropy.class })
public class AllTests {

}
