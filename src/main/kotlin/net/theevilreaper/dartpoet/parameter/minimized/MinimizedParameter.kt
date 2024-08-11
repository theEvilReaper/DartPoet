package net.theevilreaper.dartpoet.parameter.minimized

import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.property.PropertySpec

/**
 * Represents a minimized version of a parameter used in Dart, where the [TypeName] is not required.
 * This class is particularly useful in contexts like constructor creation or enumeration classes in Dart,
 * where a full parameter specification may be unnecessary.
 * By using a minimized parameter, the code becomes more concise and less complex.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @property name the name of the parameter.
 * @property self indicates whether the parameter is a self call.
 * @property type the type of the parameter.
 * @constructor creates a new instance of [MinimizedParameter].
 * @author theEvilReaper
 */
data class MinimizedParameter internal constructor(
    val name: String,
    val self: Boolean = true,
    val type: ParameterType
) {

    companion object {

        /**
         * Creates a new instance of [MinimizedParameter] using the provided [PropertySpec] and [type].
         * This is typically used when the parameter originates from a property definition.
         *
         * @param propertySpec the property specification.
         * @param type the type of the parameter.
         * @return the created [MinimizedParameter].
         */
        fun fromProperty(propertySpec: PropertySpec, type: ParameterType) = MinimizedParameter(
            name = propertySpec.name,
            type = type
        )

        /**
         * Creates a new instance of [MinimizedParameter] with the given [PropertySpec], [type], and [selfCall].
         * This method is useful when the parameter is used in a self-referencing context.
         *
         * @param propertySpec the property specification.
         * @param type the type of the parameter.
         * @param selfCall whether the parameter is a self call.
         * @return the created [MinimizedParameter].
         */
        fun fromProperty(propertySpec: PropertySpec, type: ParameterType, selfCall: Boolean) = MinimizedParameter(
            type = type,
            self = selfCall,
            name = propertySpec.name
        )

        /**
         * Creates a new instance of [MinimizedParameter] using the provided [ParameterSpec].
         * This is particularly useful for creating minimized parameters from existing parameter specifications.
         *
         * @param paramSpec the parameter specification.
         * @return the created [MinimizedParameter].
         */
        fun fromParameter(paramSpec: ParameterSpec) = MinimizedParameter(
            name = paramSpec.name,
            type = paramSpec.parameterType
        )

        /**
         * Creates a new instance of [MinimizedParameter] with the given [ParameterSpec] and [selfCall].
         * Use this method when a self-referencing parameter is needed.
         *
         * @param paramSpec the parameter specification.
         * @param selfCall whether the parameter is a self call.
         * @return the created [MinimizedParameter].
         */
        fun fromParameter(paramSpec: ParameterSpec, selfCall: Boolean) = MinimizedParameter(
            name = paramSpec.name,
            type = paramSpec.parameterType,
            self = selfCall
        )
    }
}
