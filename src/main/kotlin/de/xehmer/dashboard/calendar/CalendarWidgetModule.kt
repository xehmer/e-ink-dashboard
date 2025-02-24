package de.xehmer.dashboard.calendar

import de.xehmer.dashboard.api.DashboardApiModule
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "widget-calendar", allowedDependencies = ["base", "api"])
@Configuration
class CalendarWidgetModule(dashboardApiModule: DashboardApiModule) {
    init {
        dashboardApiModule.registerWidgetDefinition(CalendarWidgetDefinition::class)
    }
}
