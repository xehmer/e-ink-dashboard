package de.xehmer.dashboard.widgets.calendar.google

import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import de.xehmer.dashboard.widgets.calendar.CalendarWidgetData
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
@Profile("mock-google")
@Order(Ordered.HIGHEST_PRECEDENCE)
class GoogleCalendarWidgetMockDataProvider : WidgetDataProvider<GoogleCalendarWidgetDefinition, CalendarWidgetData> {

    override fun getData(
        widgetDefinition: GoogleCalendarWidgetDefinition,
        context: DashboardContext
    ): CalendarWidgetData {
        val today = LocalDate.now(context.timezone)
        val now = today.atTime(12, 0)
        return CalendarWidgetData(
            listOf(
                // all-day today
                CalendarWidgetData.AllDayCalendarEvent("Geburtstag", today, today),
                // all-day in the next days
                CalendarWidgetData.AllDayCalendarEvent("Urlaub", today.plusDays(1), today.plusDays(2)),
                // today, happening right now
                CalendarWidgetData.TimedCalendarEvent(
                    "Meeting",
                    now.minusHours(1).minusMinutes(20),
                    now.plusMinutes(10)
                ),
                // today, happening shortly after
                CalendarWidgetData.TimedCalendarEvent(
                    "Mittagessen",
                    now.plusMinutes(20),
                    now.plusMinutes(65)
                ),
                // started yesterday, ending today in the future
                CalendarWidgetData.TimedCalendarEvent(
                    "Großelternbesuch",
                    now.minusDays(1).minusHours(2),
                    now.plusHours(1).plusMinutes(30)
                ),
                // started earlier today, ending in the next days
                CalendarWidgetData.TimedCalendarEvent(
                    "Fränze",
                    now.minusHours(1).minusMinutes(40),
                    now.plusDays(2).plusHours(3)
                ),
                // starting sometime later today, extending into next day
                CalendarWidgetData.TimedCalendarEvent(
                    "geile Party",
                    today.atTime(22, 0),
                    today.plusDays(1).atTime(2, 0)
                ),
            )
        )
    }
}
