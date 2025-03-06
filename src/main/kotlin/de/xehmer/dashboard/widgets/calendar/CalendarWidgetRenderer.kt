package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.html.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle

@Service
class CalendarWidgetRenderer : WidgetRenderer<BaseCalendarWidgetDefinition, CalendarWidgetData> {
    override fun render(
        widget: Widget<BaseCalendarWidgetDefinition, CalendarWidgetData>,
        context: DashboardContext,
        target: HtmlBlockTag
    ) = with(target) {
        val today = LocalDate.now(context.timezone)
        val lastDateToRender = today.plusDays(widget.definition.maxDays)

        div("widget-calendar") {
            var dateToRender = today
            while (dateToRender <= lastDateToRender) {
                renderDate(dateToRender, widget.data.events, today, context)
                dateToRender = dateToRender.plusDays(1)
            }
        }
    }

    private fun DIV.renderDate(
        date: LocalDate,
        calendarEvents: List<CalendarWidgetData.CalendarEvent>,
        today: LocalDate,
        context: DashboardContext,
    ) {
        val wholeDayEvents = collectWholeDayEvents(calendarEvents, date)
        val timedDayEvents = collectTimedDayEvents(calendarEvents, date)
        if (wholeDayEvents.isNotEmpty() || timedDayEvents.isNotEmpty()) {
            section {
                inlineStyle {
                    marginBottom = 0.75.rem
                }

                renderDayHeadline(date, today, context)
                if (wholeDayEvents.isNotEmpty()) {
                    renderAllDayEvents(wholeDayEvents)
                }
                if (timedDayEvents.isNotEmpty()) {
                    renderTimedEvents(timedDayEvents, context)
                }
            }
        }
    }

    private fun collectWholeDayEvents(
        events: List<CalendarWidgetData.CalendarEvent>,
        day: LocalDate
    ): List<WholeDayEvent> {
        val wholeDayEvents = events.filterIsInstance<CalendarWidgetData.AllDayCalendarEvent>()
            .filter { it.firstDate <= day && it.lastDate >= day }
            .map(::WholeDayEvent)
            .toMutableList()
        wholeDayEvents += events
            .filterIsInstance<CalendarWidgetData.TimedCalendarEvent>()
            .filter { it.start.toLocalDate() < day && it.end.toLocalDate() > day }
            .map(::WholeDayEvent)
        return wholeDayEvents
    }

    private fun collectTimedDayEvents(
        events: List<CalendarWidgetData.CalendarEvent>,
        day: LocalDate
    ): List<TimedDayEvent> {
        val timedCalendarEvents = events.filterIsInstance<CalendarWidgetData.TimedCalendarEvent>()

        // collect events that start and end today (only future ends, as should be filtered by the data provider)
        val timedDayEvents: MutableList<TimedDayEvent> = timedCalendarEvents
            .filter { it.start.toLocalDate() == day && it.end.toLocalDate() == day }
            .map(TimedDayEvent::FullyContainedTimedDayEvent)
            .toMutableList()

        // add events that start today and end at a later day
        timedDayEvents += timedCalendarEvents.filter { it.start.toLocalDate() == day && it.end.toLocalDate() > day }
            .map(TimedDayEvent::StartingTimedDayEvent)

        // add events that started on an earlier date and end (later) today
        timedDayEvents += timedCalendarEvents.filter { it.start.toLocalDate() < day && it.end.toLocalDate() == day }
            .map(TimedDayEvent::EndingTimedDayEvent)

        return timedDayEvents
    }

    private fun HtmlBlockTag.renderDayHeadline(day: LocalDate, today: LocalDate, context: DashboardContext) {
        val dayName = when (day) {
            today -> "Heute"
            today.plusDays(1) -> "Morgen"
            else -> day.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, context.locale)
        }

        h3 {
            inlineStyle {
                fontSize = 1.1.rem
                marginBottom = 0.4.rem
            }
            +"$dayName, ${context.dateFormatter.format(day)}"
        }
    }

    private fun HtmlBlockTag.renderAllDayEvents(events: List<WholeDayEvent>) {
        events.forEach { event ->
            span {
                inlineStyle {
                    fontSize = 1.rem
                    border = Border(1.px, BorderStyle.solid, Color.black)
                    borderRadius = 0.25.rem
                    padding = Padding(0.1.rem)
                }
                +event.title
            }
        }
    }

    private fun HtmlBlockTag.renderTimedEvents(events: List<TimedDayEvent>, context: DashboardContext) {
        table {
            events.sortedBy {
                when (it) {
                    is TimedDayEvent.FullyContainedTimedDayEvent -> it.start
                    is TimedDayEvent.StartingTimedDayEvent -> it.start
                    is TimedDayEvent.EndingTimedDayEvent -> it.end
                }
            }.forEach { event ->
                tr {
                    td {
                        val formatter = context.timeFormatter
                        when (event) {
                            is TimedDayEvent.FullyContainedTimedDayEvent ->
                                +"${formatter.format(event.start)} - ${formatter.format(event.end)}"

                            is TimedDayEvent.EndingTimedDayEvent ->
                                +"bis ${formatter.format(event.end)}"

                            is TimedDayEvent.StartingTimedDayEvent -> {
                                val adverb = if (LocalTime.now(context.timezone).isBefore(event.start)) "ab" else "seit"
                                +"$adverb ${formatter.format(event.start)}"
                            }
                        }
                    }
                    td {
                        +event.title
                    }
                }
            }
        }
    }

    private data class WholeDayEvent(val title: String) {
        constructor(calendarEvent: CalendarWidgetData.CalendarEvent) : this(calendarEvent.title)
    }

    private sealed interface TimedDayEvent {
        val title: String

        data class FullyContainedTimedDayEvent(
            override val title: String,
            val start: LocalTime,
            val end: LocalTime
        ) : TimedDayEvent {
            constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
                title = calendarEvent.title,
                start = calendarEvent.start.toLocalTime(),
                end = calendarEvent.end.toLocalTime()
            )
        }

        data class StartingTimedDayEvent(
            override val title: String,
            val start: LocalTime
        ) : TimedDayEvent {
            constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
                title = calendarEvent.title,
                start = calendarEvent.start.toLocalTime(),
            )
        }

        data class EndingTimedDayEvent(
            override val title: String,
            val end: LocalTime
        ) : TimedDayEvent {
            constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
                title = calendarEvent.title,
                end = calendarEvent.end.toLocalTime(),
            )
        }
    }
}
