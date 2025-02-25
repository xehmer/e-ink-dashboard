package de.xehmer.dashboard.widgets.weather

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.api.WidgetDisplayDefinition

@JsonTypeName("weather")
data class WeatherWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val lat: Float,
    val lon: Float,
) : WidgetDefinition
