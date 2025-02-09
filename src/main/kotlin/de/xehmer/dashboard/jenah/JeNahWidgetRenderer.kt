package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.widgets.PreparedWidget
import de.xehmer.dashboard.widgets.WidgetRenderer
import kotlinx.html.*
import org.springframework.stereotype.Service

@Service
class JeNahWidgetRenderer : WidgetRenderer<JeNahWidgetSpec, JeNahWidgetData> {
    override fun render(widget: PreparedWidget<JeNahWidgetSpec, JeNahWidgetData>, target: HtmlBlockTag) = with(target) {
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
                    widget.data.departures.forEach { departure ->
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
