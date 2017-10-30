package org.jastacry.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All testcases in one suite.
 *
 * @author Kai Kretschmann
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TestLayerMd5Des.class, TestLayerRandom.class, TestLayerRotate.class, TestLayerTransparent.class,
        TestLayerXor.class, TestLayerFilemerge.class, TestMain.class })
public class AllTests {

}
