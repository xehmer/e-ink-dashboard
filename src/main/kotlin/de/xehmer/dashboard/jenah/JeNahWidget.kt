package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.widgets.BaseWidget
import de.xehmer.dashboard.widgets.WidgetController
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div

class JeNahWidget(spec: JeNahWidgetSpec, controller: WidgetController<JeNahWidgetSpec, JeNahWidgetData>) :
    BaseWidget<JeNahWidgetSpec, JeNahWidgetData>(spec, controller) {

    override fun renderInto(target: HtmlBlockTag) = with(target) {
        div {
            +data?.departures.toString()
        }
    }
}
