import org.adt.core.annotations.AssociatedWith
import org.adt.data.repository.DataRepositoryImpl
import org.junit.jupiter.api.Test

class SampleTest {
    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.PING)
    fun samplePingTest(){

    }
}