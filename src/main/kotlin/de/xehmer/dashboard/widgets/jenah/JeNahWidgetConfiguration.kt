package de.xehmer.dashboard.widgets.jenah

import de.xehmer.dashboard.api.ApiModule
import org.springframework.context.annotation.Configuration

/**
 * See [here](https://www.stadtwerke-jena.de/nahverkehr/privatkunden/fahrplaene.html) for the VMT web app.
 */
@Configuration
class JeNahWidgetConfiguration(apiModule: ApiModule) {
    init {
        apiModule.registerWidgetDefinition(JeNahWidgetDefinition::class)
    }
}
