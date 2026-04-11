import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaMethod
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.adt.core.annotations.AssociatedWith
import org.adt.core.annotations.RepositoryImpl

@AnalyzeClasses(packages = ["org.adt"])
class RepositoryImplementationHasTestsRule {
    @ArchTest
    fun checkRepositories(allClasses: JavaClasses) {
        val rule =
            ArchRuleDefinition.classes()
                .that().areAnnotatedWith(RepositoryImpl::class.java)
                .should(haveCorrespondingTestsWithTargetsAnnotation(allClasses))

        rule.check(allClasses)
    }

}

fun haveCorrespondingTestsWithTargetsAnnotation(allClasses: JavaClasses) =
    object : ArchCondition<JavaClass>("have pairing tests with @AssociatedWith") {
        override fun check(repositoryClass: JavaClass, events: ConditionEvents) {
            val allMethodsInProject =
                allClasses.flatMap { it.methods }
                    .plus(repositoryClass)

            repositoryClass.methods.forEach { method ->
                if (method.name == "<init>" || isStandardMethod(method)) return@forEach

                val hasTest = allMethodsInProject.any { testMethod ->
                    val annotation = testMethod.annotations.find {
                        it.rawType.isEquivalentTo(AssociatedWith::class.java)
                    } ?: return@any false

                    val targetClassValue =
                        annotation.getExplicitlyDeclaredProperty("targetClass").toString()

                    val targetMethodName =
                        annotation.getExplicitlyDeclaredProperty("method").toString()

                    val normalizedTargetClass = targetClassValue.removePrefix("class ").removeSuffix(".class")

                    repositoryClass.name.contains(normalizedTargetClass)
                            && targetMethodName == method.name
                }

                if (!hasTest) {
                    events.add(
                        SimpleConditionEvent.violated(
                            method,
                            "Method ${repositoryClass.simpleName}.${method.name} hasn't got associated .test with @AssociatedWith annotation!"
                        )
                    )
                }
            }
        }

        private fun isStandardMethod(method: JavaMethod): Boolean {
            return listOf("equals", "hashCode", "toString", "clone").contains(method.name)
        }
    }