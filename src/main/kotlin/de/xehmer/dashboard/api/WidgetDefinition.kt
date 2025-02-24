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
