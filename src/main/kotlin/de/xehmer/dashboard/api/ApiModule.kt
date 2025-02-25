package de.xehmer.dashboard.api

import de.xehmer.dashboard.api.internal.SubtypeRegisteringAnnotationIntrospector
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule
import kotlin.reflect.KClass

@ApplicationModule(id = "api", allowedDependencies = [])
@Configuration
class ApiModule(private val subtypeRegisteringAnnotationIntrospector: SubtypeRegisteringAnnotationIntrospector) {
    fun registerWidgetDefinition(widgetDefinitionClass: KClass<out WidgetDefinition>) {
        subtypeRegisteringAnnotationIntrospector.registerSubtype(widgetDefinitionClass)
    }
}
