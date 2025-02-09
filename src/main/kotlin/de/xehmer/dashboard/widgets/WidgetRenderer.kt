package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import kotlinx.html.HtmlBlockTag

fun interface WidgetRenderer<S : WidgetSpec, D : Any> {
    fun render(widget: PreparedWidget<S, D>, target: HtmlBlockTag)
}
