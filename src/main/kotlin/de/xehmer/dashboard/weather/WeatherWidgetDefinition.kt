package de.xehmer.dashboard.weather

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.api.models.WidgetDisplayDefinition

@JsonTypeName("weather")
data class WeatherWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val lat: Float,
    val lon: Float,
) : WidgetDefinition
