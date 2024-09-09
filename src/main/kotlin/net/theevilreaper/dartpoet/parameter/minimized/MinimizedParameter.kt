package net.theevilreaper.dartpoet.parameter.minimized

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.ParameterBase
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.TypeName

/**
 * Represents a minimized version of a parameter used in Dart, where the [TypeName] is not required.
 * This class is particularly useful in contexts like constructor creation or enumeration classes in Dart,
 * where a full parameter specification may be unnecessary.
 * By using a minimized parameter, the code becomes more concise and less complex.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @property name the name of the parameter
 * @property self indicates whether the parameter is a self call
 * @property type the type of the parameter
 * @constructor creates a new instance of [MinimizedParameter]
 * @author theEvilReaper
 */
class MinimizedParameter internal constructor(
    name: String,
    type: ParameterType,
    val self: Boolean = true,
    val initializer: CodeBlock? = null,
    val nullable: Boolean = false,
): ParameterBase(name, type) {

    val hasInitializer: Boolean
        get() = initializer != null

    init {
        require(!(type == ParameterType. REQUIRED && initializer != null && initializer. hasStatements())) {
            "A required parameter cannot have an initializer"
        }
    }

    /**
     * This method delegates the writing process to a [MinimizedParameter] instance, which is responsible for
     * writing the parameter details to the specified [CodeWriter].
     *
     * @param codeWriter the [CodeWriter] to which the parameter should be written
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.minimizedParameterWriter.write(this, codeWriter)
    }

    /**
     * Returns a string representation of the [MinimizedParameter].
     *
     * @return the string representation
     */
    override fun toString() = buildCodeString { write(this) }

    companion object {

        /**
         * Creates a new instance of [MinimizedParameter] using the provided [PropertySpec] and [type].
         * This is typically used when the parameter originates from a property definition
         *
         * @param propertySpec the property specification
         * @param type the type of the parameter
         * @return the created [MinimizedParameter]
         */
        fun fromProperty(propertySpec: PropertySpec, type: ParameterType = ParameterType.POSITIONAL) = MinimizedParameter(
            name = propertySpec.name,
            type = type,
            initializer = propertySpec.initBlock.build()
        )

        /**
         * Creates a new instance of [MinimizedParameter] with the given [PropertySpec], [type], and [selfCall].
         * This method is useful when the parameter is used in a self-referencing context.
         *
         * @param propertySpec the property specification
         * @param type the type of the parameter
         * @param selfCall whether the parameter is a self call
         * @return the created [MinimizedParameter]
         */
        fun fromProperty(propertySpec: PropertySpec, type: ParameterType = ParameterType.POSITIONAL, selfCall: Boolean) = MinimizedParameter(
            type = type,
            self = selfCall,
            name = propertySpec.name,
            initializer = propertySpec.initBlock.build()
        )

        /**
         * Creates a new instance of [MinimizedParameter] using the provided [ParameterSpec].
         * This is particularly useful for creating minimized parameters from existing parameter specifications.
         *
         * @param paramSpec the parameter specification
         * @return the created [MinimizedParameter]
         */
        fun fromParameter(paramSpec: ParameterSpec) = MinimizedParameter(
            name = paramSpec.name,
            type = paramSpec.parameterType,
            initializer = paramSpec.initializer
        )

        /**
         * Creates a new instance of [MinimizedParameter] with the given [ParameterSpec] and [selfCall].
         * Use this method when a self-referencing parameter is needed.
         *
         * @param paramSpec the parameter specification
         * @param selfCall whether the parameter is a self call
         * @return the created [MinimizedParameter]
         */
        fun fromParameter(paramSpec: ParameterSpec, selfCall: Boolean) = MinimizedParameter(
            name = paramSpec.name,
            type = paramSpec.parameterType,
            self = selfCall,
            initializer = paramSpec.initializer
        )
    }
}
