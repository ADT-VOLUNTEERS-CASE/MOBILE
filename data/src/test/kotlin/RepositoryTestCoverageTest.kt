import org.adt.core.testFixtures.ArchRulesHelper
import org.junit.jupiter.api.Test

class RepositoryImplementationHasTestsRule {
    @Test
    fun checkRepositories() {
        val allClasses = ArchRulesHelper.getAllClasses()
        val rule = ArchRulesHelper.getRepositoryTestCoverageRule(allClasses)

        rule.check(allClasses)
    }
}