package com.apollographql.apollo3.compiler.codegen.kotlin.file

import com.apollographql.apollo3.compiler.TargetLanguage
import com.apollographql.apollo3.compiler.codegen.Identifier
import com.apollographql.apollo3.compiler.codegen.Identifier.UNKNOWN__
import com.apollographql.apollo3.compiler.codegen.kotlin.CgFile
import com.apollographql.apollo3.compiler.codegen.kotlin.CgFileBuilder
import com.apollographql.apollo3.compiler.codegen.kotlin.KotlinContext
import com.apollographql.apollo3.compiler.codegen.kotlin.KotlinSymbols
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.addSuppressions
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.deprecatedAnnotation
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.maybeAddDeprecation
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.maybeAddDescription
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.maybeAddOptIn
import com.apollographql.apollo3.compiler.codegen.kotlin.helpers.maybeAddRequiresOptIn
import com.apollographql.apollo3.compiler.codegen.typePackageName
import com.apollographql.apollo3.compiler.internal.escapeKotlinReservedWordInEnum
import com.apollographql.apollo3.compiler.ir.IrEnum
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode

internal class EnumAsEnumBuilder(
    private val context: KotlinContext,
    private val enum: IrEnum,
    private val withUnknown: Boolean
) : CgFileBuilder {
  private val layout = context.layout
  private val packageName = layout.typePackageName()
  private val simpleName = layout.schemaTypeName(enum.name)

  private val selfClassName: ClassName
    get() = context.resolver.resolveSchemaType(enum.name)


  override fun prepare() {
    context.resolver.registerSchemaType(
        enum.name,
        ClassName(
            packageName,
            simpleName
        )
    )
  }

  override fun build(): CgFile {
    return CgFile(
        packageName = packageName,
        fileName = simpleName,
        typeSpecs = listOf(enum.toEnumClassTypeSpec())
    )
  }

  private fun IrEnum.toEnumClassTypeSpec(): TypeSpec {
    return TypeSpec
        .enumBuilder(simpleName)
        .maybeAddDescription(description)
        .primaryConstructor(primaryConstructorSpec)
        .addProperty(rawValuePropertySpec)
        .addType(companionTypeSpec())
        .apply {
          values.forEach { value ->
            addEnumConstant(value.targetName.escapeKotlinReservedWordInEnum(), value.enumConstTypeSpec())
          }
          if (withUnknown) {
            addEnumConstant("UNKNOWN__", unknownValueTypeSpec())
          }
        }
        .build()
  }

  private fun IrEnum.companionTypeSpec(): TypeSpec {
    return TypeSpec.companionObjectBuilder()
        .addProperty(typePropertySpec())
        .addProperty(knownEntriesPropertySpec())
        .apply {
          if (withUnknown) {
            // !withUnknown is a new thing, no need to add deprecated symbols there
            addFunction(knownValuesFunSpec())
          }
        }
        .addFunction(safeValueOfFunSpec())
        .build()
  }

  private fun IrEnum.knownValuesFunSpec(): FunSpec {
    return FunSpec.builder(Identifier.knownValues)
        .addKdoc("Returns all [%T] known at compile time", selfClassName)
        .addAnnotation(deprecatedAnnotation("Use knownEntries instead").toBuilder().addMember("replaceWith·=·ReplaceWith(%S)", "this.knownEntries").build())
        .returns(KotlinSymbols.Array.parameterizedBy(selfClassName))
        .addCode("return %N.toTypedArray()", Identifier.knownEntries)
        .build()
  }

  private fun IrEnum.knownEntriesPropertySpec(): PropertySpec {
    return PropertySpec.builder(Identifier.knownEntries, KotlinSymbols.List.parameterizedBy(selfClassName))
        .addKdoc("All [%T] known at compile time", selfClassName)
        .addSuppressions(enum.values.any { it.deprecationReason != null })
        .maybeAddOptIn(context.resolver, enum.values)
        .getter(
            FunSpec.getterBuilder()
                .addCode(CodeBlock.builder()
                    .add("return·listOf(\n")
                    .indent()
                    .add(
                        values.map {
                          CodeBlock.of("%N", it.targetName.escapeKotlinReservedWordInEnum())
                        }.joinToCode(",\n")
                    )
                    .unindent()
                    .add(")\n")
                    .build()
                )
                .build()
        )
        .build()
  }


  private fun IrEnum.safeValueOfFunSpec(): FunSpec {
    val entries = if (context.isTargetLanguageVersionAtLeast(TargetLanguage.KOTLIN_1_9)) "entries" else "values()"
    return FunSpec
        .builder("safeValueOf")
        .addParameter("rawValue", String::
        class)
        .returns(selfClassName)
        .addCode("return·$entries.find·{·it.rawValue·==·rawValue·} ?: ")
        .apply {
          if (withUnknown) {
            addCode(UNKNOWN__)
          } else {
            addCode("error(\"No enum value found '${'$'}rawValue'\")")
          }
        }
        .build()
  }

  private fun IrEnum.Value.enumConstTypeSpec(): TypeSpec {
    return TypeSpec.anonymousClassBuilder()
        .maybeAddDeprecation(deprecationReason)
        .maybeAddRequiresOptIn(context.resolver, optInFeature)
        .maybeAddDescription(description)
        .addSuperclassConstructorParameter("%S", name)
        .build()
  }

  private fun unknownValueTypeSpec(): TypeSpec {
    return TypeSpec
        .anonymousClassBuilder()
        .addKdoc("%L", "Auto generated constant for unknown enum values\n")
        .addSuperclassConstructorParameter("%S", UNKNOWN__)
        .build()
  }

  private val primaryConstructorSpec =
      FunSpec
          .constructorBuilder()
          .addParameter("rawValue", KotlinSymbols.String)
          .build()

  private val rawValuePropertySpec =
      PropertySpec
          .builder("rawValue", KotlinSymbols.String)
          .initializer("rawValue")
          .build()

}
