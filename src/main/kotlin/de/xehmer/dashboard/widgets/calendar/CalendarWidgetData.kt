package de.xehmer.dashboard.widgets.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class CalendarWidgetData(
    val events: List<CalendarEvent>
) {
    sealed interface CalendarEvent {
        val title: String
        val location: String?
    }

    data class AllDayCalendarEvent(
        override val title: String,
        val firstDate: LocalDate,
        val lastDate: LocalDate,
        override val location: String?,
    ) : CalendarEvent

    data class TimedCalendarEvent(
        override val title: String,
        val start: LocalDateTime,
        val end: LocalDateTime,
        override val location: String?,
    ) : CalendarEvent
}
