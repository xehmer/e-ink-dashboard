package de.xehmer.dashboard.widgets.jenah

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
import kotlinx.html.*
import org.springframework.stereotype.Service

@Service
class JeNahWidgetRenderer : WidgetRenderer<JeNahWidgetDefinition, JeNahWidgetData> {
    override fun render(
        widget: Widget<JeNahWidgetDefinition, JeNahWidgetData>,
        context: DashboardContext,
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
                            td { +departure.plannedTime.toLocalTime().format(context.timeFormatter) }
                            td {
                                +departure.predictedTime.toLocalTime().format(context.timeFormatter)
                                if (departure.delay.toMinutes() > 0) {
                                    +" (+${departure.delay.toMinutes()})"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
