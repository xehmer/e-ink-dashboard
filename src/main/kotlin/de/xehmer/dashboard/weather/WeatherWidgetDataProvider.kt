package de.xehmer.dashboard.weather

import de.xehmer.dashboard.widgets.UnpreparedWidget
import de.xehmer.dashboard.widgets.WidgetDataProvider
import org.springframework.stereotype.Service

@Service
class WeatherWidgetDataProvider : WidgetDataProvider<WeatherWidgetSpec, WeatherWidgetData> {

    override fun getData(widget: UnpreparedWidget<WeatherWidgetSpec>): WeatherWidgetData {
        return WeatherWidgetData("Weather for ${widget.spec.lat}:${widget.spec.lon}")
    }

}
