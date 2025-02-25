package de.xehmer.dashboard.core.widget

import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext

fun interface WidgetDataProvider<S : WidgetDefinition, D : Any> {
    fun getData(widgetDefinition: S, context: DashboardContext): D
}
