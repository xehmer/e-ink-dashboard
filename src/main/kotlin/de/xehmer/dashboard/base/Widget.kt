package de.xehmer.dashboard.base

import de.xehmer.dashboard.api.WidgetDefinition

data class Widget<out S : WidgetDefinition, out D : Any>(
    val definition: S,
    val data: D,
)
