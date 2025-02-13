package de.xehmer.dashboard.weather

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.models.WidgetDisplaySpec
import de.xehmer.dashboard.api.models.WidgetSpec

@JsonTypeName("weather")
data class WeatherWidgetSpec(
    override val display: WidgetDisplaySpec,
    val lat: Float,
    val lon: Float,
) : WidgetSpec
