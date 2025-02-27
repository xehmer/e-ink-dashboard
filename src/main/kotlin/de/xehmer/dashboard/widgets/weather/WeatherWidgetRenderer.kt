package de.xehmer.dashboard.widgets.weather

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import org.springframework.stereotype.Service

@Service
class WeatherWidgetRenderer : WidgetRenderer<WeatherWidgetDefinition, WeatherWidgetData> {
    override fun render(
        widget: Widget<WeatherWidgetDefinition, WeatherWidgetData>,
        context: DashboardContext,
        target: HtmlBlockTag
    ) = with(target) {
        div {
            +widget.data.weather
        }
    }
}
