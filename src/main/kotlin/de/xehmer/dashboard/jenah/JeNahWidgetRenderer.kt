package de.xehmer.dashboard.jenah

import de.xehmer.dashboard.widgets.PreparedWidget
import de.xehmer.dashboard.widgets.WidgetRenderer
import kotlinx.html.*
import org.springframework.stereotype.Service

@Service
class JeNahWidgetRenderer : WidgetRenderer<JeNahWidgetDefinition, JeNahWidgetData> {
    override fun render(
        widget: PreparedWidget<JeNahWidgetDefinition, JeNahWidgetData>,
        target: HtmlBlockTag
    ) = with(target) {
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
