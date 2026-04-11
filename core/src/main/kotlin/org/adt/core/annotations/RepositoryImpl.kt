package org.adt.core.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RepositoryImpl(
    val suppressed: Boolean = false,
)
