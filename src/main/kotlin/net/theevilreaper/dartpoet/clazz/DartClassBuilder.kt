package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartClassType
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.property.DartPropertySpec

class DartClassBuilder internal constructor(
    internal val name: String?,
    internal val classType: DartClassType
): SpecMethods<DartClassBuilder> {

    private val classMetaData: SpecData = SpecData()
    private val isAnonymousClass get() = name == null && classType == DartClassType.CLASS
    private val isEnumClass get() = classType == DartClassType.ENUM
    private val isMixinClass get() = classType == DartClassType.MIXIN

    private val propertyStack: MutableList<DartPropertySpec> = mutableListOf()

    fun property(dartPropertySpec: DartPropertySpec) = apply {
        this.propertyStack += dartPropertySpec
    }

    fun property(dartPropertySpec: () -> DartPropertySpec) = apply {
        this.propertyStack += dartPropertySpec()
    }

    override fun annotations(annotations: Iterable<AnnotationSpec>)= apply {
        this.classMetaData.annotations(annotations)
    }

    override fun annotations(annotations: () -> Iterable<AnnotationSpec>) = apply {
        this.classMetaData.annotations(annotations)
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.classMetaData.modifier(modifier)
    }

    override fun modifier(modifier: () -> DartModifier) = apply  {
        this.classMetaData.modifier(modifier)
    }

    override fun modifiers(modifiers: Iterable<DartModifier>) = apply  {
        this.classMetaData.modifiers(modifiers)
    }

    override fun modifiers(modifiers: () -> Iterable<DartModifier>) = apply  {
        this.classMetaData.modifiers(modifiers)
    }

    fun build(): DartClassSpec {
        return DartClassSpec(this)
    }
}