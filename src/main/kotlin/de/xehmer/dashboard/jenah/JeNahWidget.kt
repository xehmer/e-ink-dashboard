package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.WidgetController
import de.xehmer.dashboard.widgets.WidgetWithData
import kotlinx.html.*

class JeNahWidget(
    spec: JeNahWidgetSpec,
    context: DashboardContext,
    controller: WidgetController<JeNahWidgetSpec, JeNahWidgetData>
) : WidgetWithData<JeNahWidgetSpec, JeNahWidgetData>(spec, context, controller) {

    override fun renderInto(target: HtmlBlockTag, data: JeNahWidgetData) = with(target) {
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
                            td { +departure.plannedTime.time.toString() }
                            td {
                                +departure.predictedTime.time.toString()
                                if (departure.delay.inWholeMinutes > 0) {
                                    +" (+${departure.delay.inWholeMinutes})"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
