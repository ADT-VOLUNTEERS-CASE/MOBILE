package org.adt.core.sample

import org.adt.core.annotations.RepositoryImpl

@RepositoryImpl
class SampleRepoImpl {
    fun foo(): () -> Unit = {}
}