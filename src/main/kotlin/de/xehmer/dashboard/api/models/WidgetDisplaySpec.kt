package de.xehmer.dashboard.api.models

data class WidgetDisplaySpec(
    val startColumn: Int,
    val columnSpan: Int,
    val startRow: Int,
    val rowSpan: Int,
    val align: String? = null,
    val justify: String? = null,
)
