package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.api.WidgetDefinition
import jakarta.validation.constraints.Min

interface BaseCalendarWidgetDefinition : WidgetDefinition {
    @get:Min(1)
    val maxEvents: Int

    @get:Min(1)
    val maxDays: Int
}
