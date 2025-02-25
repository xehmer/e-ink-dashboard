package de.xehmer.dashboard.calendar

import de.xehmer.dashboard.base.DashboardContext
import de.xehmer.dashboard.base.WidgetDataProvider
import org.springframework.stereotype.Service
import kotlinx.datetime.LocalDateTime

data class CalendarEvent(
    val title: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String?
)

data class CalendarWidgetData(
    val events: List<CalendarEvent>
)

@Service
class CalendarWidgetDataProvider(
    private val googleCalendarService: GoogleCalendarService
) : WidgetDataProvider<CalendarWidgetDefinition, CalendarWidgetData> {
    override fun getData(widgetDefinition: CalendarWidgetDefinition, context: DashboardContext): CalendarWidgetData {
        return CalendarWidgetData(
            events = googleCalendarService.getCalendarEvents(widgetDefinition, context)
        )
    }
}
