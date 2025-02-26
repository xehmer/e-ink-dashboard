package de.xehmer.dashboard.widgets.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class CalendarWidgetData(
    val events: List<CalendarEvent>
) {
    data class CalendarEvent(
        val title: String,
        val startDate: LocalDate,
        val startTime: LocalTime?,
        val endDate: LocalDate,
        val endTime: LocalTime?,
        val location: String,
    )
}
