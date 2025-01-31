package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.BaseWidget
import de.xehmer.dashboard.widgets.WidgetController
import kotlinx.html.*

class JeNahWidget(
    spec: JeNahWidgetSpec,
    context: DashboardContext,
    controller: WidgetController<JeNahWidgetSpec, JeNahWidgetData>
) : BaseWidget<JeNahWidgetSpec, JeNahWidgetData>(spec, context, controller) {

    override fun renderInto(target: HtmlBlockTag) = with(target) {
        val data = preparedData ?: return@with

        div {
            table {
                thead {
                    tr {
                        td { +"Linie" }
                        td { +"Richtung" }
                        td { +"geplant" }
                        td { +"voraussichtlich" }
                    }
                }
                tbody {
                    data.departures.forEach { departure ->
                        tr {
                            td { +departure.line.displayString }
                            td { +departure.destination }
                            td { +departure.plannedTime.toString() }
                            td { +departure.predictedTime.toString() }
                        }
                    }
                }
            }
        }
    }
}
