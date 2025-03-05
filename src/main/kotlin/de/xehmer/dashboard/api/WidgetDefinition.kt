package de.xehmer.dashboard.api

import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.Valid
import jakarta.validation.constraints.Min

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "widgetType",
)
interface WidgetDefinition {
    @get:Valid
    val display: WidgetDisplayDefinition
}

data class WidgetDisplayDefinition(
    @field:Min(1)
    val startColumn: Int,
    @field:Min(1)
    val columnSpan: Int,
    @field:Min(1)
    val startRow: Int,
    @field:Min(1)
    val rowSpan: Int,
    val align: String? = null,
    val justify: String? = null,
)
