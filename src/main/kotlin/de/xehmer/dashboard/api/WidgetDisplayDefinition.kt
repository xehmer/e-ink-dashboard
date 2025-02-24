package de.xehmer.dashboard.api

data class WidgetDisplayDefinition(
    val startColumn: Int,
    val columnSpan: Int,
    val startRow: Int,
    val rowSpan: Int,
    val align: String? = null,
    val justify: String? = null,
)
