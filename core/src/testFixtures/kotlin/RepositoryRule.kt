import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaMethod
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.adt.core.annotations.AssociatedWith
import org.adt.core.annotations.RepositoryImpl
import java.nio.file.Paths

/**
 * Used to obtain project's ArchUnit rules for further usage.
 *
 * Contains static [allClasses] and [allMethods] variables(scan classes are unlikely to change during tests)
 * as well as [getRepositoryTestCoverageRule] function and some private helpers.
 */
object ArchRulesHelper {
    var allClasses: Array<JavaClass>?
    var allMethods: Array<JavaMethod>?

    var annotatedWithAssociatedMethods: Array<JavaMethod> = arrayOf()

    init {
        allClasses = getAllClasses()?.toTypedArray()
        allMethods = allClasses?.flatMap { it.methods }?.toTypedArray()

        if (!allClasses.isNullOrEmpty()) {
            annotatedWithAssociatedMethods = getAllAnnotatedClasses(
                allMethods,
                AssociatedWith::class.java,
            ) ?: arrayOf()
        }
    }

    fun getRepositoryTestCoverageRule(allClasses: JavaClasses?): ArchRule {
        return ArchRuleDefinition.classes().that().areAnnotatedWith(RepositoryImpl::class.java)
            .should(haveCorrespondingTestsWithTargetsAnnotation(allClasses)).allowEmptyShould(true)
    }

    /**
     * Iterates through given classes annotated with [RepositoryImpl]
     * and verifies, that every method has at least one corresponding test with [AssociatedWith] annotation.
     *
     * @see RepositoryImpl
     * @see AssociatedWith
     **/
    private fun haveCorrespondingTestsWithTargetsAnnotation(allClasses: JavaClasses?) =
        object : ArchCondition<JavaClass>(
            "Each method in @RepositoryImpl must have a corresponding test with @AssociatedWith"
        ) {
            override fun check(repositoryClass: JavaClass, events: ConditionEvents) {
                if (allClasses.isNullOrEmpty()) return

                repositoryClass.methods.asSequence().filter { isMethodEligibleForTesting(it) }
                    .forEach { method ->
                        if (isMethodHasAssociatedTest(method, repositoryClass)) return@forEach

                        events.add(
                            SimpleConditionEvent.violated(
                                method,
                                "Method ${repositoryClass.simpleName}.${method.name} hasn't got associated .test with @AssociatedWith annotation!."
                            )
                        )
                    }
            }

            private fun isMethodEligibleForTesting(method: JavaMethod): Boolean {
                return !listOf(
                    "<init>", "equals", "hashCode", "toString", "clone", $$"json$lambda$0"
                ).contains(method.name)
            }
        }

    /**
     * Scan for all project classes and return nullable [JavaClasses]
     *
     * Scan paths are defined in Gradle module configuration.
     */
    fun getAllClasses(): JavaClasses? {
        val classDirs = System.getProperty("project.class.dirs")?.split(",")?.map { Paths.get(it) }
            ?: emptyList()

        if (classDirs.isEmpty()) {
            println("Warning: No class directories found")
            return null
        }

        return ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .importPaths(classDirs)
    }

    private fun getAllAnnotatedClasses(
        allMethods: Array<JavaMethod>?, annotationClass: Class<out Annotation>
    ): Array<JavaMethod>? {
        return allMethods?.filter { testMethod ->
            testMethod.hasAnnotation(annotationClass)
        }?.toTypedArray()
    }

    private fun isMethodHasAssociatedTest(method: JavaMethod, repositoryClass: JavaClass): Boolean {
        if (repositoryClass.getAnnotationOfType(RepositoryImpl::class.java).suppressed)
            return true

        return annotatedWithAssociatedMethods.any {
            val annotation = it.getAnnotationOfType(AssociatedWith::class.java)

            val targetClassName = annotation.targetClass.simpleName.toString()
            val targetMethodName = annotation.method

            targetMethodName == method.name && repositoryClass.name.contains(targetClassName)
        }
    }

    private fun JavaMethod.hasAnnotation(annotationClass: Class<out Annotation>): Boolean {
        return this.annotations.any { it.rawType.isEquivalentTo(annotationClass) }
    }
}

