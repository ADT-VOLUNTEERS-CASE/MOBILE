package org.adt.core.annotations

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class ImplicitUsageProcessor(private val codeGenerator: CodeGenerator,
): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ImplicitUsage::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { classDeclaration ->
            generateSuppressedWrapper(classDeclaration)
        }

        return emptyList()
    }

    private fun generateSuppressedWrapper(classDeclaration: KSClassDeclaration) {
        val pkgName = classDeclaration.packageName.asString()
        val simpleName = classDeclaration.simpleName.asString()
        val wrapperName = "${simpleName}_ImplicitUsageWrapper"

        val typeSpec = TypeSpec.classBuilder(wrapperName)
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "UNUSED")
                    .build()
            )
            .addKdoc("Implicit usage thorough reflection. Suppresses unused warning.\n", simpleName)
            .build()

        val fileSpec = FileSpec.builder(pkgName, wrapperName)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(codeGenerator, aggregating = false)
    }
}
