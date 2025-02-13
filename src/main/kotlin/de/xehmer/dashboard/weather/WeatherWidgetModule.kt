package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.SubtypeRegisteringAnnotationIntrospector
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

@ApplicationModule
@Configuration
class WeatherWidgetModule(subtypeRegisteringAnnotationIntrospector: SubtypeRegisteringAnnotationIntrospector) {
    init {
        subtypeRegisteringAnnotationIntrospector.registerSubtype(WeatherWidgetSpec::class)
    }
}
