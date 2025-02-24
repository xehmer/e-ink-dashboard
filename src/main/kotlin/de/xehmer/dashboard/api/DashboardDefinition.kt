package de.xehmer.dashboard.api

data class DashboardDefinition(
    val context: DashboardContextDefinition,
    val display: DashboardDisplayDefinition,
    val widgets: List<WidgetDefinition>,
)
