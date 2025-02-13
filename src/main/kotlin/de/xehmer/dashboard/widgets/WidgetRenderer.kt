package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import kotlinx.html.HtmlBlockTag

fun interface WidgetRenderer<S : WidgetDefinition, D : Any> {
    fun render(widget: PreparedWidget<S, D>, target: HtmlBlockTag)
}
