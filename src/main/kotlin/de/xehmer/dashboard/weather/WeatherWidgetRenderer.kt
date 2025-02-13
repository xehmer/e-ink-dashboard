package de.xehmer.dashboard.weather

import de.xehmer.dashboard.widgets.PreparedWidget
import de.xehmer.dashboard.widgets.WidgetRenderer
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import org.springframework.stereotype.Service

@Service
class WeatherWidgetRenderer : WidgetRenderer<WeatherWidgetSpec, WeatherWidgetData> {
    override fun render(widget: PreparedWidget<WeatherWidgetSpec, WeatherWidgetData>, target: HtmlBlockTag) =
        with(target) {
            div {
                +widget.data.weather
            }
        }
}
