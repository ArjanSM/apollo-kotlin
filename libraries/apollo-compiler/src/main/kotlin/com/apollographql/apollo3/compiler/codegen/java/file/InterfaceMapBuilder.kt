package com.apollographql.apollo3.compiler.codegen.java.file

import com.apollographql.apollo3.compiler.capitalizeFirstLetter
import com.apollographql.apollo3.compiler.codegen.java.CodegenJavaFile
import com.apollographql.apollo3.compiler.codegen.java.JavaClassBuilder
import com.apollographql.apollo3.compiler.codegen.java.JavaContext
import com.apollographql.apollo3.compiler.codegen.typeBuilderPackageName
import com.apollographql.apollo3.compiler.ir.IrInterface
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

internal class InterfaceMapBuilder(
    private val context: JavaContext,
    private val iface: IrInterface,
) : JavaClassBuilder {
  private val layout = context.layout
  private val packageName = layout.typeBuilderPackageName()
  private val simpleName = "${iface.name.capitalizeFirstLetter()}Map"

  override fun prepare() {
    context.resolver.registerMapType(iface.name, ClassName.get(packageName, simpleName))
  }

  override fun build(): CodegenJavaFile {
    return CodegenJavaFile(
        packageName = packageName,
        typeSpec = iface.mapTypeSpec()
    )
  }

  private fun IrInterface.mapTypeSpec(): TypeSpec {
    return TypeSpec
        .interfaceBuilder(simpleName)
        .addModifiers(Modifier.PUBLIC)
        .addSuperinterfaces(
            implements.map {
              ClassName.get(packageName, "${it.capitalizeFirstLetter()}Map")
            }
        )
        .build()
  }
}
