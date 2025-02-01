package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.models.WeatherWidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.WidgetController
import de.xehmer.dashboard.widgets.WidgetWithData
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div

class WeatherWidget(
    spec: WeatherWidgetSpec,
    context: DashboardContext,
    controller: WidgetController<WeatherWidgetSpec, WeatherWidgetData>
) : WidgetWithData<WeatherWidgetSpec, WeatherWidgetData>(spec, context, controller) {

    override fun renderInto(target: HtmlBlockTag, data: WeatherWidgetData) = with(target) {
        div {
            +data.weather
        }
    }
}
