package de.xehmer.dashboard.widgets.jenah

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.api.WidgetDisplayDefinition

@JsonTypeName("jenah")
data class JeNahWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val apiClient: String,
    val apiAuthorization: String,
    val station: String,
) : WidgetDefinition
