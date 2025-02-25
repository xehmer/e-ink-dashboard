package de.xehmer.dashboard.calendar

import de.xehmer.dashboard.api.WidgetDisplayDefinition
import de.xehmer.dashboard.base.DashboardContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

@ExtendWith(MockitoExtension::class)
class CalendarWidgetTest {

    @Mock
    private lateinit var googleCalendarService: GoogleCalendarService

    @Test
    fun `test widget definition configuration`() {
        val definition = CalendarWidgetDefinition(
            display = WidgetDisplayDefinition(
                startColumn = 0,
                columnSpan = 2,
                startRow = 0,
                rowSpan = 2
            ),
            calendarId = "test@gmail.com",
            clientId = "test-client-id",
            clientSecret = "test-client-secret",
            maxEvents = 5
        )

        assertEquals("test@gmail.com", definition.calendarId)
        assertEquals(5, definition.maxEvents)
    }

    @Test
    fun `test calendar widget data provider`() {
        val definition = CalendarWidgetDefinition(
            display = WidgetDisplayDefinition(
                startColumn = 0,
                columnSpan = 2,
                startRow = 0,
                rowSpan = 2
            ),
            calendarId = "test@gmail.com",
            clientId = "test-client-id",
            clientSecret = "test-client-secret"
        )

        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val nowLocal = now.toLocalDateTime(timeZone)
        val mockEvents = listOf(
            CalendarEvent(
                title = "Test Event",
                startTime = nowLocal,
                endTime = now.plus(1.hours).toLocalDateTime(timeZone),
                location = "Test Location"
            )
        )

        val context = DashboardContext(
            timezone = TimeZone.currentSystemDefault(),
            locale = Locale.getDefault()
        )

        `when`(googleCalendarService.getCalendarEvents(definition, context)).thenReturn(mockEvents)

        val provider = CalendarWidgetDataProvider(googleCalendarService)
        val result = provider.getData(definition, context)

        assertEquals(mockEvents, result.events)
        assertEquals("Test Event", result.events.first().title)
        assertEquals("Test Location", result.events.first().location)
    }
}
