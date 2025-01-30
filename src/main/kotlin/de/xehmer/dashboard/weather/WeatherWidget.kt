package de.xehmer.dashboard.weather

import de.xehmer.dashboard.api.models.WeatherWidgetSpec
import de.xehmer.dashboard.utils.inlineStyle
import de.xehmer.dashboard.widgets.BaseWidget
import de.xehmer.dashboard.widgets.WidgetController
import kotlinx.css.fontSize
import kotlinx.css.px
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.id

class WeatherWidget(spec: WeatherWidgetSpec, controller: WidgetController<WeatherWidgetSpec, WeatherWidgetData>) :
    BaseWidget<WeatherWidgetSpec, WeatherWidgetData>(spec, controller) {

    override fun renderInto(target: HtmlBlockTag) = with(target) {
        div {
            id = "widget-weather"
            inlineStyle {
                fontSize = 10.px
            }

            +data?.weather.orEmpty()
        }
    }
}
