package org.duttydev.financegrid

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.duttydev.financegrid")

        noClasses()
            .that()
            .resideInAnyPackage("org.duttydev.financegrid.service..")
            .or()
            .resideInAnyPackage("org.duttydev.financegrid.repository..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..org.duttydev.financegrid.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses)
    }
}
