package de.xehmer.dashboard.api.models

data class DashboardSpec(
    val context: DashboardContextSpec,
    val display: DashboardDisplaySpec,
    val widgets: List<WidgetSpec>,
)
