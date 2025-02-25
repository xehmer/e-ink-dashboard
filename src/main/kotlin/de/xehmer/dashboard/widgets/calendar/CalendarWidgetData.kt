package de.xehmer.dashboard.widgets.calendar

import kotlinx.datetime.LocalDateTime

data class CalendarWidgetData(
    val events: List<CalendarEvent>
) {
    data class CalendarEvent(
        val title: String,
        val start: LocalDateTime,
        val end: LocalDateTime,
        val allDay: Boolean,
        val location: String,
    )
}
