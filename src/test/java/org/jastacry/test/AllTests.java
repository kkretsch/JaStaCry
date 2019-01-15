package org.jastacry.test;

import org.jastacry.layer.TestAbstractBasicLayer;
import org.jastacry.layer.TestLayerAesCbc;
import org.jastacry.layer.TestLayerAesEcb;
import org.jastacry.layer.TestLayerAsciiTransport;
import org.jastacry.layer.TestLayerFilemerge;
import org.jastacry.layer.TestLayerMd5Des;
import org.jastacry.layer.TestLayerRandom;
import org.jastacry.layer.TestLayerReverse;
import org.jastacry.layer.TestLayerRotate;
import org.jastacry.layer.TestLayerTransparent;
import org.jastacry.layer.TestLayerXor;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;


/**
 * All test cases in one suite.
 *
 * @author Kai Kretschmann
 *
 */
@RunWith(JUnitPlatform.class)
@SelectClasses({
    TestExportEncryption.class, TestConfig.class, TestEntropy.class, TestLayerAesCbc.class, TestLayerAesEcb.class,
    TestLayerAsciiTransport.class, TestLayerFilemerge.class, TestLayerMd5Des.class, TestLayerRandom.class, TestLayerReverse.class,
    TestLayerRotate.class, TestLayerTransparent.class, TestLayerXor.class, TestInputOutput.class, TestMain.class, TestLocale.class,
    TestAbstractBasicLayer.class, TestGlobalFunctions.class
})
public class AllTests
{
}
