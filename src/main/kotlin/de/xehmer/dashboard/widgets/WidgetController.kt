package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec

fun interface WidgetController<S : WidgetSpec, D> {
    fun getData(spec: S): D?
}
