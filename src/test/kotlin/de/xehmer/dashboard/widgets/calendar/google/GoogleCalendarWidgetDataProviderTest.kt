package de.xehmer.dashboard.widgets.calendar.google

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import de.xehmer.dashboard.api.ApiModule
import de.xehmer.dashboard.api.WidgetDisplayDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import kotlinx.datetime.TimeZone
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.mock.env.MockEnvironment
import java.util.*

@Tag("integration")
class GoogleCalendarWidgetDataProviderTest {

    private val uut = GoogleCalendarWidgetDataProvider(
        Mockito.mock(ApiModule::class.java),
        MockEnvironment().withProperty("spring.application.name", "e-ink-dashboard")
    )

    private val om = ObjectMapper()
        .registerModules(KotlinModule.Builder().build())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    @Test
    fun shouldWork() {
        val serviceAccountJson = this.javaClass.getResourceAsStream("/secret/service-account.json") ?: return
        val serviceAccountDefinition =
            om.readValue<GoogleCalendarWidgetDefinition.ServiceAccountCredentialsDefinition>(serviceAccountJson)

        val privateHttpClientEnvJson = this.javaClass.getResourceAsStream("/http-client.private.env.json") ?: return
        val privateHttpClientEnv = om.readTree(privateHttpClientEnvJson)
        val calendarId = privateHttpClientEnv.get("dev")?.get("googlecalendar")?.get("calendarId")?.asText() ?: return

        val widgetDefinition = GoogleCalendarWidgetDefinition(
            display = WidgetDisplayDefinition(0, 0, 0, 0),
            serviceAccount = serviceAccountDefinition,
            calendarId = calendarId
        )
        val context = DashboardContext(
            timezone = TimeZone.of("Europe/Berlin"),
            locale = Locale.GERMANY
        )

        val result = uut.getData(widgetDefinition, context)

        assertThat(result).isNotNull()
    }
}
