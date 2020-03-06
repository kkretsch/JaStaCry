package org.jastacry.test;

import org.jastacry.layer.Layer;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

/**
 * Test of Main function.
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
}
