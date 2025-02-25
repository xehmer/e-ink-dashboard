package de.xehmer.dashboard.widgets.weather

import de.xehmer.dashboard.api.ApiModule
import org.springframework.context.annotation.Configuration

@Configuration
class WeatherWidgetConfiguration(apiModule: ApiModule) {
    init {
        apiModule.registerWidgetDefinition(WeatherWidgetDefinition::class)
    }
}
