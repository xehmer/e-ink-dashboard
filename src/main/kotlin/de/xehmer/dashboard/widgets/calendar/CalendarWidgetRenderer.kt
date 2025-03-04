package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetRenderer
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
        div("widget-calendar") {
            val today = Clock.System.now().toLocalDateTime(context.timezone).date
            val daysToShow = mutableListOf(today)
            for (i in 1..widget.definition.dateMax) {
                daysToShow.add(today.plus(i, DateTimeUnit.DAY))
            }

            for (day in daysToShow) {
                val wholeDayEvents = collectWholeDayEvents(widget.data.events, day)
                val timedDayEvents = collectTimedDayEvents(widget.data.events, day)
                if (wholeDayEvents.isNotEmpty() || timedDayEvents.isNotEmpty()) {
                    section {
                        renderDayHeadline(day, today, context.locale)
                        if (wholeDayEvents.isNotEmpty()) {
                            renderAllDayEvents(wholeDayEvents)
                        }
                        if (timedDayEvents.isNotEmpty()) {
                            renderTimedEvents(timedDayEvents, context.locale)
                        }
                    }
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
        // collect events starting and ending today
        val timedDayEvents = timedCalendarEvents
            .filter { it.start.date == day && it.end.date == day }
            .map(::TimedDayEvent)
            .toMutableList()
        // add events only ending today (but that started on a prior day)
        timedDayEvents += timedCalendarEvents.filter { it.start.date < day && it.end.date == day }
            .map { TimedDayEvent(title = it.title, location = it.location, end = it.end.time) }
        // add events only starting today (but that end on a later day)
        timedDayEvents += timedCalendarEvents.filter { it.start.date == day && it.end.date > day }
            .map { TimedDayEvent(title = it.title, location = it.location, start = it.start.time) }
        return timedDayEvents
    }

    private fun HtmlBlockTag.renderDayHeadline(day: LocalDate, today: LocalDate, locale: Locale) {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)
        val dateString = day.toJavaLocalDate().format(dateFormatter)
        val dayName = when (day) {
            today -> "Today"
            today.plus(1, DateTimeUnit.DAY) -> "Tomorrow"
            else -> day.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, locale)
        }

        h3 {
            +"$dayName, $dateString"
        }
    }

    private fun HtmlBlockTag.renderAllDayEvents(events: List<WholeDayEvent>) {
        events.forEach { event ->
            span("widget-calendar-event-allday-pill") {
                text(event.title)
                event.location?.let { location -> text(" ($location)") }
            }
        }
    }

    private fun HtmlBlockTag.renderTimedEvents(events: List<TimedDayEvent>, locale: Locale) {
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)

        table {
            events.forEach { event ->
                tr {
                    td {
                        if (event.start == null) {
                            text("bis ${event.end!!.toJavaLocalTime().format(timeFormatter)}")
                        } else if (event.end == null) {
                            text("seit ${event.start.toJavaLocalTime().format(timeFormatter)}")
                        } else {
                            val start = event.start.toJavaLocalTime().format(timeFormatter)
                            val end = event.end.toJavaLocalTime().format(timeFormatter)
                            text("$start - $end")
                        }
                    }
                    td {
                        text(event.title)
                        event.location?.let { location ->
                            br()
                            text(" ($location)")
                        }
                    }
                }
            }
        }

    }

    private data class WholeDayEvent(val title: String, val location: String?) {
        constructor(calendarEvent: CalendarWidgetData.CalendarEvent) : this(calendarEvent.title, calendarEvent.location)
    }

    private data class TimedDayEvent(
        val title: String,
        val location: String?,
        val start: LocalTime? = null,
        val end: LocalTime? = null
    ) {
        constructor(calendarEvent: CalendarWidgetData.TimedCalendarEvent) : this(
            title = calendarEvent.title,
            location = calendarEvent.location,
            start = calendarEvent.start.time,
            end = calendarEvent.end.time
        )
    }
}
