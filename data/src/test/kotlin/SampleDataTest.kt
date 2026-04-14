import org.adt.core.annotations.AssociatedWith
import org.adt.data.repository.DataRepositoryImpl
import org.junit.jupiter.api.Test

class SampleDataTest {
    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.PING)
    fun samplePingTest(){

    }
}