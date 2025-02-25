package de.xehmer.dashboard.core.widget

import de.xehmer.dashboard.api.WidgetDefinition

data class Widget<out S : WidgetDefinition, out D : Any>(
    val definition: S,
    val data: D,
)
