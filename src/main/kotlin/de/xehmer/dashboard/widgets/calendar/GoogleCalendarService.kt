package de.xehmer.dashboard.widgets.calendar

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.UserCredentials
import de.xehmer.dashboard.core.dashboard.DashboardContext
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import org.springframework.stereotype.Service
import java.util.*
import java.util.TimeZone as JavaTimeZone

@Service
class GoogleCalendarService {
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    fun getCalendarEvents(widgetDefinition: CalendarWidgetDefinition, context: DashboardContext): List<CalendarEvent> {
        val credentials = UserCredentials.newBuilder()
            .setClientId(widgetDefinition.clientId)
            .setClientSecret(widgetDefinition.clientSecret)
            .build()

        val httpCredentials = HttpCredentialsAdapter(credentials)
        val calendar = Calendar.Builder(httpTransport, jsonFactory, httpCredentials)
            .setApplicationName("E-Ink Dashboard")
            .build()

        val javaTimeZone = JavaTimeZone.getTimeZone(context.timezone.id)
        val now = DateTime(Date(), javaTimeZone)
        val events = calendar.events().list(widgetDefinition.calendarId)
            .setMaxResults(widgetDefinition.maxEvents)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()

        return events.items.map { event ->
            CalendarEvent(
                title = event.summary,
                startTime = Instant.fromEpochMilliseconds(
                    event.start.dateTime?.value ?: event.start.date.value
                ).toLocalDateTime(context.timezone),
                endTime = Instant.fromEpochMilliseconds(
                    event.end.dateTime?.value ?: event.end.date.value
                ).toLocalDateTime(context.timezone),
                location = event.location
            )
        }
    }
}
