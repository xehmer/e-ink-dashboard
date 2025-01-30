package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec

fun interface WidgetController<S : WidgetSpec, D : WidgetData> {

    fun getData(spec: S): D?

    companion object {

        val NOOP = WidgetController<WidgetSpec, WidgetData> { _ -> null }
    }
}
