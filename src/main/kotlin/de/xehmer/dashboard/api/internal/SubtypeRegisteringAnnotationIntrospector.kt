package de.xehmer.dashboard.api.internal

import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.jsontype.NamedType
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * [AnnotationIntrospector][com.fasterxml.jackson.databind.AnnotationIntrospector] implementation that resolves
 * programmatically provided subtypes.
 *
 * To be used instead of [com.fasterxml.jackson.databind.ObjectMapper.registerSubtypes] since that does not work for
 * Swagger, unfortunately. See [here](https://github.com/swagger-api/swagger-core/issues/4225) for one example of the
 * unresolved issue in the Swagger codebase.
 */
@Component
class SubtypeRegisteringAnnotationIntrospector : NopAnnotationIntrospector() {

    private val registeredSubtypes: MutableSet<Class<*>> = mutableSetOf()

    fun registerSubtype(type: KClass<*>) {
        registeredSubtypes.add(type.java)
    }

    override fun findSubtypes(a: Annotated?): List<NamedType>? =
        if (a == null) {
            emptyList()
        } else {
            registeredSubtypes.filter { a.rawType.isAssignableFrom(it) }
                .map { NamedType(it) }
                .ifEmpty { null }
        }
}
