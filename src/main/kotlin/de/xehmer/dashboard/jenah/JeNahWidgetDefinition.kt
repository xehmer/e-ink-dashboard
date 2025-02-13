package de.xehmer.dashboard.jenah

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.api.models.WidgetDisplayDefinition

@JsonTypeName("jenah")
data class JeNahWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val apiClient: String,
    val apiAuthorization: String,
    val station: String,
) : WidgetDefinition
