package de.xehmer.dashboard.calendar

import de.xehmer.dashboard.api.SubtypeRegisteringAnnotationIntrospector
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

@ApplicationModule
@Configuration
class CalendarWidgetModule(subtypeRegisteringAnnotationIntrospector: SubtypeRegisteringAnnotationIntrospector) {
    init {
        subtypeRegisteringAnnotationIntrospector.registerSubtype(CalendarWidgetDefinition::class)
    }
}
