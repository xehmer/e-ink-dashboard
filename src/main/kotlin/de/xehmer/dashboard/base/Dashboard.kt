package de.xehmer.dashboard.base

import de.xehmer.dashboard.api.DashboardDisplayDefinition
import de.xehmer.dashboard.api.WidgetDefinition
import kotlinx.datetime.TimeZone
import java.util.*

data class DashboardContext(
    val timezone: TimeZone,
    val locale: Locale,
)

data class UnpreparedDashboard(
    val display: DashboardDisplayDefinition,
    val context: DashboardContext,
    val widgets: List<WidgetDefinition>
)

data class PreparedDashboard(
    val display: DashboardDisplayDefinition,
    val context: DashboardContext,
    val widgets: List<Widget<*, *>>
)
