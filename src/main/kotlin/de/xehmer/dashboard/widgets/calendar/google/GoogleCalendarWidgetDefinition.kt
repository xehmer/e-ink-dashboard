package de.xehmer.dashboard.widgets.calendar.google

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDisplayDefinition
import de.xehmer.dashboard.widgets.calendar.BaseCalendarWidgetDefinition
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@JsonTypeName("googlecalendar")
data class GoogleCalendarWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    @field:Valid
    val serviceAccount: ServiceAccountCredentialsDefinition,
    @field:NotBlank
    val calendarId: String,
    override val maxEvents: Int,
    override val maxDays: Long,
) : BaseCalendarWidgetDefinition {

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
