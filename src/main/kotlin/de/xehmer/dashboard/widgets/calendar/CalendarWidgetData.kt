package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import kotlinx.datetime.LocalDateTime
import org.springframework.stereotype.Service

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
