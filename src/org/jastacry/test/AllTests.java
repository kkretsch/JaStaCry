package org.jastacry.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestLayerMd5Des.class, TestLayerRandom.class,
		TestLayerRotate.class, TestLayerTransparent.class, TestLayerXor.class })
public class AllTests {

}
