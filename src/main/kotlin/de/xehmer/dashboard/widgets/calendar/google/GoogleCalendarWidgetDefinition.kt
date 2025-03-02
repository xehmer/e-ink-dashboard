package de.xehmer.dashboard.widgets.calendar.google

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.api.WidgetDisplayDefinition

@JsonTypeName("googlecalendar")
data class GoogleCalendarWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val serviceAccount: ServiceAccountCredentialsDefinition,
    val calendarId: String,
) : WidgetDefinition {

    data class ServiceAccountCredentialsDefinition(
        @JsonProperty(value = "project_id")
        val projectId: String,
        @JsonProperty(value = "private_key_id")
        val privateKeyId: String,
        @JsonProperty(value = "private_key")
        val privateKey: String,
        @JsonProperty(value = "client_email")
        val clientEmail: String,
        @JsonProperty(value = "client_id")
        val clientId: String,
        @JsonProperty(value = "token_uri")
        val tokenUri: String,
        @JsonProperty(value = "universe_domain")
        val universeDomain: String,
    )
}
