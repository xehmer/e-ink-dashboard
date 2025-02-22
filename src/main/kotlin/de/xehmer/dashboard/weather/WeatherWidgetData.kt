package de.xehmer.dashboard.weather

import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.WidgetDataProvider
import org.springframework.stereotype.Service

data class WeatherWidgetData(
    val weather: String
)

@Service
class WeatherWidgetDataProvider : WidgetDataProvider<WeatherWidgetDefinition, WeatherWidgetData> {

    override fun getData(widgetDefinition: WeatherWidgetDefinition, context: DashboardContext): WeatherWidgetData {
        return WeatherWidgetData("Weather for ${widgetDefinition.lat}:${widgetDefinition.lon}")
    }

}
