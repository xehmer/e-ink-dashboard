package de.xehmer.dashboard.api

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "widgetType",
)
interface WidgetDefinition {
    val display: WidgetDisplayDefinition
}

data class WidgetDisplayDefinition(
    val startColumn: Int,
    val columnSpan: Int,
    val startRow: Int,
    val rowSpan: Int,
    val align: String? = null,
    val justify: String? = null,
)
