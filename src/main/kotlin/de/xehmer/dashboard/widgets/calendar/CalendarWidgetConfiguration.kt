package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.api.ApiModule
import org.springframework.context.annotation.Configuration

@Configuration
class CalendarWidgetConfiguration(apiModule: ApiModule) {
    init {
        apiModule.registerWidgetDefinition(CalendarWidgetDefinition::class)
    }
}
