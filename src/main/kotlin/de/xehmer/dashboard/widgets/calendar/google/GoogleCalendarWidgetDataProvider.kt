package de.xehmer.dashboard.widgets.calendar.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import de.xehmer.dashboard.api.ApiModule
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import de.xehmer.dashboard.widgets.calendar.CalendarWidgetData
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.net.URI
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class GoogleCalendarWidgetDataProvider(apiModule: ApiModule, environment: Environment) :
    WidgetDataProvider<GoogleCalendarWidgetDefinition, CalendarWidgetData> {

    init {
        apiModule.registerWidgetDefinition(GoogleCalendarWidgetDefinition::class)
    }

    private val applicationName = environment.getProperty("spring.application.name")
        ?: throw IllegalStateException("Missing 'spring.application.name' environment property")

    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    override fun getData(
        widgetDefinition: GoogleCalendarWidgetDefinition,
        context: DashboardContext
    ): CalendarWidgetData {
        val credentials = createServiceAccountCredentials(widgetDefinition)
        val httpCredentials = HttpCredentialsAdapter(credentials)
        val calendar = Calendar.Builder(httpTransport, jsonFactory, httpCredentials)
            .setApplicationName(applicationName)
            .build()

        val nowDateTime = DateTime(Instant.now().toEpochMilli())
        val endInstant = Instant.now().plus(widgetDefinition.maxDays - 1, ChronoUnit.DAYS)
        val endDateTime = DateTime(endInstant.toEpochMilli())
        val events = calendar.events().list(widgetDefinition.calendarId).apply {
            timeZone = context.timezone.id
            timeMin = nowDateTime
            timeMax = endDateTime
            maxResults = widgetDefinition.maxEvents
            orderBy = "startTime"
            singleEvents = true
        }.execute()

        return CalendarWidgetData(events = events.items.map { createCalendarEvent(it, context) })
    }

    private fun createServiceAccountCredentials(widgetDefinition: GoogleCalendarWidgetDefinition): ServiceAccountCredentials? =
        ServiceAccountCredentials.newBuilder().apply {
            projectId = widgetDefinition.serviceAccount.projectId
            privateKeyId = widgetDefinition.serviceAccount.privateKeyId
            setPrivateKeyString(widgetDefinition.serviceAccount.privateKey)
            clientEmail = widgetDefinition.serviceAccount.clientEmail
            clientId = widgetDefinition.serviceAccount.clientId
            tokenServerUri = URI.create(widgetDefinition.serviceAccount.tokenUri)
            universeDomain = widgetDefinition.serviceAccount.universeDomain
            scopes = listOf(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS_READONLY)
        }.build()

    private fun createCalendarEvent(event: Event, context: DashboardContext): CalendarWidgetData.CalendarEvent {
        return when {
            event.start?.dateTime == null && event.start?.date?.isDateOnly == true
                    && event.end?.dateTime == null && event.end?.date?.isDateOnly == true ->
                createAllDayCalendarEvent(event)

            event.start?.dateTime != null && event.end?.dateTime != null ->
                createTimedCalendarEvent(event, context)

            else -> throw IllegalArgumentException("Event $event not supported")
        }
    }

    private fun createAllDayCalendarEvent(event: Event): CalendarWidgetData.CalendarEvent {
        return CalendarWidgetData.AllDayCalendarEvent(
            title = event.summary,
            firstDate = event.start.date.toLocalDate(),
            lastDate = event.end.date.toLocalDate().minusDays(1L)
        )
    }

    private fun createTimedCalendarEvent(event: Event, context: DashboardContext): CalendarWidgetData.CalendarEvent {
        return CalendarWidgetData.TimedCalendarEvent(
            title = event.summary,
            start = event.start.dateTime.toLocalDateTime(context),
            end = event.end.dateTime.toLocalDateTime(context),
        )
    }

    private fun DateTime.toLocalDate(): LocalDate =
        LocalDate.parse(this.toStringRfc3339(), DateTimeFormatter.ISO_LOCAL_DATE)

    private fun DateTime.toLocalDateTime(context: DashboardContext): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(this.value), context.timezone)
}
