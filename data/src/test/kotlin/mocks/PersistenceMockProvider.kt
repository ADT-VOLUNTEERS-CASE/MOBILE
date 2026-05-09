package org.adt.data.mocks

import io.mockk.coEvery
import io.mockk.mockk
import org.adt.data.abstraction.PersistenceRepository
import org.adt.data.mocks.RetrofitMockProvider.authenticatedUser
import org.adt.data.mocks.RetrofitMockProvider.tokenStore

object PersistenceMockProvider {
    fun createMock(): PersistenceRepository {
        return mockk(relaxed = true) {
            coEvery {
                saveTokens(
                    any(),
                    any(),
                )
            } answers {
                tokenStore = Pair(firstArg(), secondArg())
            }

            coEvery { getToken() } answers {
                tokenStore.first
            }

            coEvery { getRefreshToken() } answers {
                tokenStore.second
            }

            coEvery { authorized() } answers {
                tokenStore.first.isNotBlank() && tokenStore.second.isNotBlank()
            }

            coEvery { removeToken() } answers {
                tokenStore = Pair("", "")
                authenticatedUser = null
            }
        }
    }
}