package de.xehmer.dashboard.calendar

import com.fasterxml.jackson.annotation.JsonTypeName
import de.xehmer.dashboard.api.models.WidgetDisplaySpec
import de.xehmer.dashboard.api.models.WidgetSpec

@JsonTypeName("calendar")
data class CalendarWidgetSpec(
    override val display: WidgetDisplaySpec,
    val calendarId: String,
) : WidgetSpec
