package de.xehmer.dashboard.jenah

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.models.WidgetDisplaySpec
import de.xehmer.dashboard.api.models.WidgetSpec

@JsonTypeName("jenah")
data class JeNahWidgetSpec(
    override val display: WidgetDisplaySpec,
    val apiClient: String,
    val apiAuthorization: String,
    val station: String,
) : WidgetSpec
