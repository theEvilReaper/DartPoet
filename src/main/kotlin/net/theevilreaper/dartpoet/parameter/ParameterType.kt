package net.theevilreaper.dartpoet.parameter

/**
 * The [ParameterType] enumeration is a helper class which helps to categorise the parameters which are added
 * to several language features that allows that. When a spec objects should be created that contains parameters,
 * it needs to sort them into different data structures to know what parameter types are given. The first approach is
 * to filter them by given properties which works pretty good but runs into different problems. Sometimes the checked
 * properties doesn't tell enough to sort them into the right data structure.
 * @since 1.0.0
 * @author theEvilReaper
 */
enum class ParameterType {

    STANDARD,
    NAMED,
    REQUIRED,
    OPTIONAL,
}