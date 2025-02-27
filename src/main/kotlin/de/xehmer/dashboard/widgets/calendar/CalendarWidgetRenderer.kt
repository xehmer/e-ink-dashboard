package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.html.*
import org.springframework.stereotype.Service

@Service
class CalendarWidgetRenderer : WidgetRenderer<WidgetDefinition, CalendarWidgetData> {
    override fun render(
        widget: Widget<WidgetDefinition, CalendarWidgetData>,
        context: DashboardContext,
        target: HtmlBlockTag
    ) = with(target) {
        div {
            classes = setOf("widget-calendar")
            section {
                h3 { +"Today" }

                val now = Clock.System.now().toLocalDateTime(context.timezone)
                val today = now.date
                widget.data.events
                    .filterIsInstance<CalendarWidgetData.AllDayCalendarEvent>()
                    .filter { it.firstDate <= today && it.lastDate >= today }
                    .let { renderAllDayEvents(it) }

                widget.data.events
                    .filterIsInstance<CalendarWidgetData.TimedCalendarEvent>()
                    .filter { it.start <= now && it.end >= now }
            }
            section {
                h3 { +"Tomorrow" }

                val tomorrow = Clock.System.now().toLocalDateTime(context.timezone).date.plus(1, DateTimeUnit.DAY)
                widget.data.events
                    .filterIsInstance<CalendarWidgetData.AllDayCalendarEvent>()
                    .filter { it.firstDate <= tomorrow && it.lastDate >= tomorrow }
                    .let { renderAllDayEvents(it) }
            }
            table {
                thead {
                    tr {
                        td { +"" }
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

    private fun HtmlBlockTag.renderAllDayEvents(events: List<CalendarWidgetData.AllDayCalendarEvent>) {
        events.forEach { event ->
            span {
                classes = setOf("widget-calendar-event-allday-pill")
                +event.title
                event.location?.let { location -> +" ($location)" }
            }
        }
    }
}
