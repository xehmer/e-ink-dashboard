package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.dashboard.DashboardContext

data class UnpreparedWidget<out S : WidgetDefinition>(
    val definition: S,
    val context: DashboardContext,
)

data class PreparedWidget<out S : WidgetDefinition, out D : Any>(
    val definition: S,
    val context: DashboardContext,
    val data: D,
)
