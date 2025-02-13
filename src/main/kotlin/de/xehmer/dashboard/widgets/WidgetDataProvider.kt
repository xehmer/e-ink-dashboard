package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition

fun interface WidgetDataProvider<S : WidgetDefinition, D : Any> {
    fun getData(widget: UnpreparedWidget<S>): D
}
