package de.xehmer.dashboard.api

data class DashboardDefinition(
    val context: DashboardContextDefinition,
    val display: DashboardDisplayDefinition,
    val widgets: List<WidgetDefinition>,
)

data class DashboardContextDefinition(
    val timeZone: String,
    val locale: String,
)

data class DashboardDisplayDefinition(
    val width: Int,
    val height: Int,
    val columnCount: Int,
    val rowCount: Int,
)
