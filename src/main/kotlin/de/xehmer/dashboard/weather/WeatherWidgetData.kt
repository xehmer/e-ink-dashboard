package de.xehmer.dashboard.weather

import de.xehmer.dashboard.widgets.UnpreparedWidget
import de.xehmer.dashboard.widgets.WidgetDataProvider
import org.springframework.stereotype.Service

data class WeatherWidgetData(
    val weather: String
)

@Service
class WeatherWidgetDataProvider : WidgetDataProvider<WeatherWidgetDefinition, WeatherWidgetData> {

    override fun getData(widget: UnpreparedWidget<WeatherWidgetDefinition>): WeatherWidgetData {
        return WeatherWidgetData("Weather for ${widget.definition.lat}:${widget.definition.lon}")
    }

}
