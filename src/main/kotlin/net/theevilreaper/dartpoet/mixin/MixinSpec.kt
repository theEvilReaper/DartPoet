package net.theevilreaper.dartpoet.mixin

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * A [MixinSpec] represents a mixin declaration in Dart.
 * It implements [TypeSpec] so it can be used anywhere a top-level type declaration is expected.
 *
 * @author theEvilReaper
 * @since 2.4.0
 */
class MixinSpec internal constructor(
    builder: MixinBuilder,
) : TypeSpec {
    override val name: String = builder.name
    val onTypes: List<TypeName> = builder.onTypes.toImmutableList()
    val interfaces: List<TypeName> = builder.interfaces.toImmutableList()
    val genericCasts: Set<TypeName> = builder.genericCasts.toImmutableSet()
    val properties: Set<PropertySpec> = builder.properties.toImmutableSet()
    val functions: Set<FunctionSpec> = builder.functions.toImmutableSet()
    val operators: Set<DartOperatorSpec> = builder.operators.toImmutableSet()
    val constants: Set<ConstantPropertySpec> = builder.constants.toImmutableSet()
    val typeDefs: List<AbstractTypeDef<*>> = builder.typeDefs.toImmutableList()
    val annotations: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    val modifiers: Set<DartModifier> = builder.specData.modifiers.toImmutableSet()
    val endsWithNewLine: Boolean = builder.endWithNewLine
    val isBase: Boolean = DartModifier.BASE in modifiers

    internal val hasNoContent: Boolean
        get() = properties.isEmpty() && functions.isEmpty() && operators.isEmpty() && constants.isEmpty() && typeDefs.isEmpty()

    init {
        check(name.isNotBlank() && !name.contains(" ")) { "The mixin name can not be empty or contain whitespaces" }
        val invalidModifiers = modifiers.filter { it != DartModifier.PUBLIC && it != DartModifier.PRIVATE && it != DartModifier.BASE }
        check(invalidModifiers.isEmpty()) { "A mixin can only have the 'base' modifier, but got: $invalidModifiers" }
        check(onTypes.size == onTypes.distinct().size) { "Duplicate 'on' type(s) found: ${onTypes.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}" }
        check(interfaces.size == interfaces.distinct().size) { "Duplicate interface type(s) found: ${interfaces.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}" }
        check(operators.size == operators.distinctBy { it.operator }.size) { "Duplicate operator(s) found: ${operators.groupingBy { it.operator }.eachCount().filterValues { it > 1 }.map { it.key.symbol }}" }
    }

    override fun write(codeWriter: CodeWriter) {
        WriterHelper.mixinWriter.write(this, codeWriter)
    }

    override fun toString(): String = buildCodeString { write(this) }

    fun toBuilder(): MixinBuilder {
        val builder = MixinBuilder(this.name)
        builder.onTypes.addAll(this.onTypes)
        builder.interfaces.addAll(this.interfaces)
        builder.genericCasts.addAll(this.genericCasts)
        builder.properties.addAll(this.properties)
        builder.functions.addAll(this.functions)
        builder.operators.addAll(this.operators)
        builder.constants.addAll(this.constants)
        builder.typeDefs.addAll(this.typeDefs)
        builder.specData.annotations.addAll(this.annotations)
        builder.specData.modifiers.addAll(this.modifiers)
        builder.endWithNewLine = this.endsWithNewLine
        return builder
    }

    companion object {
        @JvmStatic
        fun builder(name: String) = MixinBuilder(name)

        @JvmStatic
        fun builder(name: String, vararg modifiers: DartModifier) = MixinBuilder(name, *modifiers)
    }
}
