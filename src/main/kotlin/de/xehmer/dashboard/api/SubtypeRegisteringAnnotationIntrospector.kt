package de.xehmer.dashboard.api

import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.jsontype.NamedType
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

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
