package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.util.NULLABLE_CHAR

/**
 * Represents a generic type parameter, optionally bounded, such as `T` or `T extends Comparable`.
 *
 * [emit] writes only the bare name — never the bound. A type parameter is referenced as a plain
 * type everywhere else in a class/extension body (property types, parameter types, return types),
 * and `T extends Bar` would be invalid Dart in any of those positions. The bound is rendered only
 * by [renderDeclaration], called exclusively by the writers that emit a generic *declaration*
 * (as opposed to a type *reference*): `ClassWriter.writeGenericArguments` and
 * `ExtensionSpec.genericDeclaration`.
 *
 * @param name the name of the type parameter
 * @param bound the optional bound of the type parameter (Dart has no multi-bound syntax, so this
 *              is a single [TypeName], not a list)
 * @param isNullable whether this type name can be null (default is false)
 * @since 2.1.0
 * @author theEvilReaper
 */
class TypeVariableName internal constructor(
    val name: String,
    val bound: TypeName? = null,
    isNullable: Boolean = false
) : TypeName(isNullable) {

    init {
        require(name.trim().isNotEmpty()) { "The name of a TypeVariableName can't be empty" }
    }

    /**
     * Emits only the bare [name] of this type parameter — never [bound]. See the class KDoc for why.
     */
    override fun emit(out: CodeWriter): CodeWriter {
        out.emit(name)
        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }
        return out
    }

    override fun copy(nullable: Boolean): TypeVariableName = TypeVariableName(name, bound, nullable)

    override fun getRawData(): String = name

    companion object {

        /**
         * Renders [typeName] as it should appear at a generic-declaration site: the bare name,
         * plus ` extends <bound>` if [typeName] is a [TypeVariableName] with a non-null [bound].
         * Not for general type references — only for declaration sites.
         *
         * Builds each piece via [buildCodeString] + [TypeName.emit] rather than [TypeName.toString],
         * so a nullable bound doesn't double its `?` (toString() goes through `cachedString`, which
         * re-appends `?` on top of whatever `emit()` already wrote).
         *
         * @param typeName the type name to render as a declaration
         * @return the rendered declaration string
         */
        internal fun renderDeclaration(typeName: TypeName): String {
            val name = buildCodeString { typeName.emit(this) }
            val bound = (typeName as? TypeVariableName)?.bound ?: return name
            return "$name extends ${buildCodeString { bound.emit(this) }}"
        }
    }
}
