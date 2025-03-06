package de.xehmer.dashboard.widgets.calendar

import java.time.LocalDate
import java.time.LocalDateTime

data class CalendarWidgetData(
    val events: List<CalendarEvent>
) {
    sealed interface CalendarEvent {
        val title: String
    }

    data class AllDayCalendarEvent(
        override val title: String,
        val firstDate: LocalDate,
        val lastDate: LocalDate,
    ) : CalendarEvent

    data class TimedCalendarEvent(
        override val title: String,
        val start: LocalDateTime,
        val end: LocalDateTime,
    ) : CalendarEvent
}
