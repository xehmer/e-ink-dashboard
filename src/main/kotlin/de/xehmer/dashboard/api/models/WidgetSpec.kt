package de.xehmer.dashboard.api.models

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "widgetType",
)
interface WidgetSpec {
    val display: WidgetDisplaySpec
}
