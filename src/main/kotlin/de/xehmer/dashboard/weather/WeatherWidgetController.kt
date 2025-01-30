package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.models.WeatherWidgetSpec
import de.xehmer.dashboard.widgets.WidgetController
import de.xehmer.dashboard.widgets.WidgetTypeRegistry
import org.springframework.stereotype.Service

@Service
final class WeatherWidgetController(
    widgetTypeRegistry: WidgetTypeRegistry
) : WidgetController<WeatherWidgetSpec, WeatherWidgetData> {

    init {
        widgetTypeRegistry.registerWidgetType(WeatherWidgetSpec::class, this, ::WeatherWidget)
    }

    override fun getData(spec: WeatherWidgetSpec): WeatherWidgetData {
        return WeatherWidgetData("Weather for ${spec.lat}:${spec.lon}")
    }
}
