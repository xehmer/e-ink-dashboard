package de.xehmer.dashboard.weather

import de.xehmer.dashboard.widgets.Widget
import de.xehmer.dashboard.widgets.WidgetRenderer
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import org.springframework.stereotype.Service

@Service
class WeatherWidgetRenderer : WidgetRenderer<WeatherWidgetDefinition, WeatherWidgetData> {
    override fun render(
        widget: Widget<WeatherWidgetDefinition, WeatherWidgetData>,
        target: HtmlBlockTag
    ) = with(target) {
        div {
            +widget.data.weather
        }
    }
}
