package org.jastacry.test;

import org.jastacry.layer.Layer;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

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
    public static final ArchRule noJavaUtilLoggin = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    public static final ArchRule noStdStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    public static final ArchRule noGenericExceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    public static final ArchRule noJodaTime = NO_CLASSES_SHOULD_USE_JODATIME;
}
