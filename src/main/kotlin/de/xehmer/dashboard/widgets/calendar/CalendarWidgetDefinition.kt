package de.xehmer.dashboard.widgets.calendar

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.api.WidgetDisplayDefinition

@JsonTypeName("calendar")
data class CalendarWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val clientId: String,
    val clientSecret: String,
    val calendarId: String,
    val maxEvents: Int = 5
) : WidgetDefinition
