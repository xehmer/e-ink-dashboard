package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.DashboardApiModule
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.ApplicationModule

/**
 * See [here](https://www.stadtwerke-jena.de/nahverkehr/privatkunden/fahrplaene.html) for the VMT web app.
 */
@ApplicationModule(id = "widget-jenah", allowedDependencies = ["base", "api"])
@Configuration
class JeNahWidgetModule(dashboardApiModule: DashboardApiModule) {
    init {
        dashboardApiModule.registerWidgetDefinition(JeNahWidgetDefinition::class)
    }
}
