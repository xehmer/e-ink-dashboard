package de.xehmer.dashboard.calendar

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.api.WidgetDisplayDefinition

@JsonTypeName("calendar")
data class CalendarWidgetDefinition(
    override val display: WidgetDisplayDefinition,
    val calendarId: String,
) : WidgetDefinition
