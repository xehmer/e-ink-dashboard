package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition

data class Widget<out S : WidgetDefinition, out D : Any>(
    val definition: S,
    val data: D,
)
