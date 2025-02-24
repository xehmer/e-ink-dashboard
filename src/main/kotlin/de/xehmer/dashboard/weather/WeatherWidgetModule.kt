package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.DashboardApiModule
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "widget-weather", allowedDependencies = ["base", "api"])
@Configuration
class WeatherWidgetModule(dashboardApiModule: DashboardApiModule) {
    init {
        dashboardApiModule.registerWidgetDefinition(WeatherWidgetDefinition::class)
    }
}
