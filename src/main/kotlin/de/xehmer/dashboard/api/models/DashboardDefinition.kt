package de.xehmer.dashboard.api.models

data class DashboardDefinition(
    val context: DashboardContextDefinition,
    val display: DashboardDisplayDefinition,
    val widgets: List<WidgetDefinition>,
)
