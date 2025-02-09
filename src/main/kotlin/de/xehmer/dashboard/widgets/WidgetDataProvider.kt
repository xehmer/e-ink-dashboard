package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec

fun interface WidgetDataProvider<S : WidgetSpec, D : Any> {
    fun getData(widget: UnpreparedWidget<S>): D
}
