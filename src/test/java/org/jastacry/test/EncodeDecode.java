package org.jastacry.test;

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
    TestMainSingleEncode.class, TestMainSingleDecode.class
})
public class EncodeDecode
{
}
