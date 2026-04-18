package org.adt.core.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AssociatedWith(
    val targetClass: KClass<*>,
    val method: String,
)
