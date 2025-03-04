package de.xehmer.dashboard.widgets.calendar

import de.xehmer.dashboard.api.WidgetDefinition

interface BaseCalendarWidgetDefinition : WidgetDefinition {
    val maxResults: Int
    val dateMax: Int
}
