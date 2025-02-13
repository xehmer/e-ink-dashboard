package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.SubtypeRegisteringAnnotationIntrospector
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

@ApplicationModule
@Configuration
class JeNahWidgetModule(subtypeRegisteringAnnotationIntrospector: SubtypeRegisteringAnnotationIntrospector) {
    init {
        subtypeRegisteringAnnotationIntrospector.registerSubtype(JeNahWidgetDefinition::class)
    }
}
