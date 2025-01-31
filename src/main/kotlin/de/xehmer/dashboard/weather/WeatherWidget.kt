package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.models.WeatherWidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.BaseWidget
import de.xehmer.dashboard.widgets.WidgetController
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div

class WeatherWidget(
    spec: WeatherWidgetSpec,
    context: DashboardContext,
    controller: WidgetController<WeatherWidgetSpec, WeatherWidgetData>
) : BaseWidget<WeatherWidgetSpec, WeatherWidgetData>(spec, context, controller) {

    override fun renderInto(target: HtmlBlockTag) = with(target) {
        val data = preparedData ?: return

        div {
            +data.weather
        }
    }
}
