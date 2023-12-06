package org.jastacry.test;

import org.jastacry.layer.TestAbstractBasicLayer;
import org.jastacry.layer.TestLayerAesCbc;
import org.jastacry.layer.TestLayerAesCtr;
import org.jastacry.layer.TestLayerAesEcb;
import org.jastacry.layer.TestLayerAesGcm;
import org.jastacry.layer.TestLayerAsciiTransport;
import org.jastacry.layer.TestLayerAppend;
import org.jastacry.layer.TestLayerFilemerge;
import org.jastacry.layer.TestLayerMd5Des;
import org.jastacry.layer.TestLayerRandom;
import org.jastacry.layer.TestLayerReadWrite;
import org.jastacry.layer.TestLayerReverse;
import org.jastacry.layer.TestLayerRotate;
import org.jastacry.layer.TestLayerTransparent;
import org.jastacry.layer.TestLayerXor;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * All test cases in one suite.
 *
 * @author Kai Kretschmann
 *
 */
@Suite
@SelectClasses({
    TestMainBytes.class,
    TestConfig.class, TestEntropy.class, TestExportEncryption.class, TestGlobalFunctions.class, TestInputOutput.class, TestLocale.class,
    TestMain.class,
    TestAbstractBasicLayer.class,
    TestLayerAesCbc.class, TestLayerAesCtr.class, TestLayerAesEcb.class, TestLayerAesGcm.class, TestLayerAppend.class,
    TestLayerAsciiTransport.class, TestLayerFilemerge.class, TestLayerMd5Des.class, TestLayerRandom.class, TestLayerReadWrite.class, TestLayerReverse.class,
    TestLayerRotate.class, TestLayerTransparent.class, TestLayerXor.class,
    TestArch.class
})
public class AllTests
{
}
