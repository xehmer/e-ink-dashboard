package de.xehmer.dashboard.weather

import de.xehmer.dashboard.base.DashboardContext
import de.xehmer.dashboard.base.WidgetDataProvider
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
