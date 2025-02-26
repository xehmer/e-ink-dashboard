package de.xehmer.dashboard.widgets.calendar.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.EventDateTime
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import de.xehmer.dashboard.api.ApiModule
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import de.xehmer.dashboard.widgets.calendar.CalendarWidgetData
import kotlinx.datetime.*
import org.springframework.stereotype.Service
import java.net.URI
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.TimeZone as KotlinTimeZone

@Service
class GoogleCalendarWidgetDataProvider(apiModule: ApiModule) :
    WidgetDataProvider<GoogleCalendarWidgetDefinition, CalendarWidgetData> {

    init {
        apiModule.registerWidgetDefinition(GoogleCalendarWidgetDefinition::class)
    }

    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    override fun getData(
        widgetDefinition: GoogleCalendarWidgetDefinition,
        context: DashboardContext
    ): CalendarWidgetData = CalendarWidgetData(events = getCalendarEvents(widgetDefinition, context))

    fun getCalendarEvents(
        widgetDefinition: GoogleCalendarWidgetDefinition,
        context: DashboardContext
    ): List<CalendarWidgetData.CalendarEvent> {
        val credentials = ServiceAccountCredentials.newBuilder().apply {
            projectId = widgetDefinition.serviceAccount.projectId
            privateKeyId = widgetDefinition.serviceAccount.privateKeyId
            setPrivateKeyString(widgetDefinition.serviceAccount.privateKey)
            clientEmail = widgetDefinition.serviceAccount.clientEmail
            clientId = widgetDefinition.serviceAccount.clientId
            tokenServerUri = URI.create(widgetDefinition.serviceAccount.tokenUri)
            universeDomain = widgetDefinition.serviceAccount.universeDomain
            scopes = listOf(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS_READONLY)
        }.build()

        val httpCredentials = HttpCredentialsAdapter(credentials)
        val calendar = Calendar.Builder(httpTransport, jsonFactory, httpCredentials)
            .setApplicationName("E-Ink Dashboard")
            .build()

        val now = DateTime(Clock.System.now().toEpochMilliseconds())
        val inTwoDays = DateTime(Clock.System.now().plus(2.days).toEpochMilliseconds())
        val events = calendar.events().list(widgetDefinition.calendarId).apply {
            timeMin = now
            timeMax = inTwoDays
            maxResults = 20
            orderBy = "startTime"
            singleEvents = true
        }.execute()

        return events.items.map { event ->
            CalendarWidgetData.CalendarEvent(
                title = event.summary.orEmpty(),
                startDate = event.start.extractDate(context.timezone),
                startTime = event.start.extractTime(context.timezone),
                endDate = event.end.extractDate(context.timezone),
                endTime = event.end.extractTime(context.timezone),
                location = event.location.orEmpty()
            )
        }
    }

}

private fun EventDateTime?.extractDate(timezone: KotlinTimeZone): LocalDate {
    val timestamp = this?.dateTime?.value ?: this?.date?.value
    require(timestamp != null) { "Could not convert date time $this" }
    return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(timezone).date
}

private fun EventDateTime?.extractTime(timezone: KotlinTimeZone): LocalTime? {
    val timestamp = this?.dateTime?.value ?: return null
    return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(timezone).time
}
