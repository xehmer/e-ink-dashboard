package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.datetime.*
import kotlinx.html.*
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

@Service
class CalendarWidgetRenderer : WidgetRenderer<BaseCalendarWidgetDefinition, CalendarWidgetData> {
    override fun render(
        widget: Widget<BaseCalendarWidgetDefinition, CalendarWidgetData>,
        context: DashboardContext,
        target: HtmlBlockTag
    ) = with(target) {
        val today = Clock.System.now().toLocalDateTime(context.timezone).date
        val lastDateToRender = today.plus(widget.definition.maxDays, DateTimeUnit.DAY)

        div("widget-calendar") {
            var dateToRender = today
            while (dateToRender <= lastDateToRender) {
                renderDate(dateToRender, widget.data.events, today, context.locale)
                dateToRender = dateToRender.plus(1, DateTimeUnit.DAY)
            }

        }
    }

    private fun DIV.renderDate(
        date: LocalDate,
        calendarEvents: List<CalendarWidgetData.CalendarEvent>,
        today: LocalDate,
        locale: Locale
    ) {
        val wholeDayEvents = collectWholeDayEvents(calendarEvents, date)
        val timedDayEvents = collectTimedDayEvents(calendarEvents, date)
        if (wholeDayEvents.isNotEmpty() || timedDayEvents.isNotEmpty()) {
            section {
                inlineStyle {
                    marginBottom = 0.75.rem
                }

                renderDayHeadline(date, today, locale)
                if (wholeDayEvents.isNotEmpty()) {
                    renderAllDayEvents(wholeDayEvents)
                }
                if (timedDayEvents.isNotEmpty()) {
                    renderTimedEvents(timedDayEvents, locale)
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
            .filter { it.start.date < day && it.end.date > day }
            .map(::WholeDayEvent)
        return wholeDayEvents
    }

    private fun collectTimedDayEvents(
        events: List<CalendarWidgetData.CalendarEvent>,
        day: LocalDate
    ): List<TimedDayEvent> {
        val timedCalendarEvents = events.filterIsInstance<CalendarWidgetData.TimedCalendarEvent>()
        val timedDayEvents: MutableList<TimedDayEvent> = timedCalendarEvents
            .filter { it.start.date == day && it.end.date == day }
            .map(TimedDayEvent::FullyContainedTimedDayEvent)
            .toMutableList()
        timedDayEvents += timedCalendarEvents.filter { it.start.date == day && it.end.date > day }
            .map(TimedDayEvent::StartingTimedDayEvent)
        timedDayEvents += timedCalendarEvents.filter { it.start.date < day && it.end.date == day }
            .map(TimedDayEvent::EndingTimedDayEvent)
        return timedDayEvents
    }

    private fun HtmlBlockTag.renderDayHeadline(day: LocalDate, today: LocalDate, locale: Locale) {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)
        val dayName = when (day) {
            today -> "Today"
            today.plus(1, DateTimeUnit.DAY) -> "Tomorrow"
            else -> day.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, locale)
        }

        h3 {
            inlineStyle {
                fontSize = 1.1.rem
                marginBottom = 0.4.rem
            }
            +"$dayName, ${dateFormatter.format(day)}"
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

    private fun HtmlBlockTag.renderTimedEvents(events: List<TimedDayEvent>, locale: Locale) {
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)

        table {
            events.forEach { event ->
                tr {
                    td {
                        when (event) {
                            is TimedDayEvent.FullyContainedTimedDayEvent ->
                                +"${timeFormatter.format(event.start)} - ${timeFormatter.format(event.end)}"

                            is TimedDayEvent.EndingTimedDayEvent ->
                                +"bis ${timeFormatter.format(event.end)}"

                            is TimedDayEvent.StartingTimedDayEvent ->
                                +"seit ${timeFormatter.format(event.start)}"
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
                start = calendarEvent.start.time,
                end = calendarEvent.end.time
            )
        }

        data class StartingTimedDayEvent(
            override val title: String,
            val start: LocalTime
        ) : TimedDayEvent {
            constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
                title = calendarEvent.title,
                start = calendarEvent.start.time,
            )
        }

        data class EndingTimedDayEvent(
            override val title: String,
            val end: LocalTime
        ) : TimedDayEvent {
            constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
                title = calendarEvent.title,
                end = calendarEvent.end.time,
            )
        }
    }
}

private fun DateTimeFormatter.format(time: LocalTime): String = format(time.toJavaLocalTime())

private fun DateTimeFormatter.format(date: LocalDate): String = format(date.toJavaLocalDate())
