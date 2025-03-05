package de.xehmer.dashboard.api

import jakarta.validation.Valid
import jakarta.validation.constraints.Min

data class DashboardDefinition(
    @field:Valid
    val context: DashboardContextDefinition,
    @field:Valid
    val display: DashboardDisplayDefinition,
    @field:Valid
    val widgets: List<WidgetDefinition>,
)

data class DashboardContextDefinition(
    val timeZone: String,
    val locale: String,
)

data class DashboardDisplayDefinition(
    @field:Min(1)
    val width: Int,
    @field:Min(1)
    val height: Int,
    @field:Min(1)
    val columnCount: Int,
    @field:Min(1)
    val rowCount: Int,
)
