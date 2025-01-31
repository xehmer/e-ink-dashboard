package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext

fun interface WidgetController<S : WidgetSpec, D> {
    fun getData(spec: S, context: DashboardContext): D?
}
