package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.code.writer.*
import org.jetbrains.annotations.ApiStatus.Internal
import org.jetbrains.annotations.ApiStatus.NonExtendable

/**
 * A helper class that holds references to writer implementations.
 * Maintaining a single instance of each writer improves runtime performance
 * by avoiding the overhead of creating new instances for each write operation.
 *
 * Since writer classes do not store any state, creating new instances is unnecessary.
 *
 * **Note**: Update this class only when adding or removing writer implementations.
 *
 * @since 1.0.0
 * author theEvilReaper
 */
@Internal
@NonExtendable
internal object WriterHelper {

    internal val annotationWriter by lazy { AnnotationWriter() }
    internal val classWriter by lazy { ClassWriter() }
    internal val constantPropertyWriter by lazy { ConstantPropertyWriter() }
    internal val constructorWriter by lazy { ConstructorWriter() }
    internal val fileWriter by lazy { DartFileWriter() }
    internal val enumPropertyWriter by lazy { EnumPropertyWriter() }
    internal val extensionWriter by lazy { ExtensionWriter() }
    internal val factoryWriter by lazy { FactoryWriter() }
    internal val functionWriter by lazy { FunctionWriter() }
    internal val parameterWriter by lazy { ParameterWriter() }
    internal val propertyWriter by lazy { PropertyWriter() }
    internal val typeDefWriter by lazy { TypeDefWriter() }
}