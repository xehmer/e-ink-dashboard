package de.xehmer.dashboard.widgets.calendar.google

import de.xehmer.dashboard.api.ApiModule
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleCalendarWidgetConfiguration(apiModule: ApiModule) {
    init {
        apiModule.registerWidgetDefinition(GoogleCalendarWidgetDefinition::class)
    }
}
