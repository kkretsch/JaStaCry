package org.jastacry.test;

import org.jastacry.layer.Layer;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static com.tngtech.archunit.library.GeneralCodingRules.USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.THROW_GENERIC_EXCEPTIONS;

/**
 * Test of Architectural stuff.
 *
 * @author Kai Kretschmann
 *
 */
@AnalyzeClasses(
        packages = "org.jastacry", 
        importOptions = 
            ImportOption.DoNotIncludeTests.class
    )
public class TestArch
{

    @ArchTest
    public static final ArchRule inheritanceRule = ArchRuleDefinition.classes()
            .that().implement(Layer.class)
            .should().haveSimpleNameEndingWith("Layer");

    @ArchTest
    public static final ArchRule noJavaUtilLoggin = ArchRuleDefinition.noClasses()
        .should(USE_JAVA_UTIL_LOGGING)
        .because("slf4j and logback/log4j2 should be used instead of java.util logger");

    @ArchTest
    public static final ArchRule noStdStreams = ArchRuleDefinition.noClasses()
        .should(ACCESS_STANDARD_STREAMS)
        .because("No stdout or stderr usage allowed");

    @ArchTest
    public static final ArchRule noGenericExceptions = ArchRuleDefinition.noClasses()
        .should(THROW_GENERIC_EXCEPTIONS)
        .because("No generic exception should be thrown");
}
