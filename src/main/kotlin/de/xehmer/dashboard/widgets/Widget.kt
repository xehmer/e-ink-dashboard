package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext

data class UnpreparedWidget<S : WidgetSpec>(
    val spec: S,
    val context: DashboardContext,
)

data class PreparedWidget<S : WidgetSpec, D : Any>(
    val spec: S,
    val context: DashboardContext,
    val data: D,
)
