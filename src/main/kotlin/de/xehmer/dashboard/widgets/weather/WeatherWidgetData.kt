package de.xehmer.dashboard.widgets.weather

import de.xehmer.dashboard.api.ApiModule
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import org.springframework.stereotype.Service

data class WeatherWidgetData(
    val weather: String
)

@Service
class WeatherWidgetDataProvider(apiModule: ApiModule) : WidgetDataProvider<WeatherWidgetDefinition, WeatherWidgetData> {

    init {
        apiModule.registerWidgetDefinition(WeatherWidgetDefinition::class)
    }

    override fun getData(widgetDefinition: WeatherWidgetDefinition, context: DashboardContext): WeatherWidgetData {
        return WeatherWidgetData("Weather for ${widgetDefinition.lat}:${widgetDefinition.lon}")
    }

}
