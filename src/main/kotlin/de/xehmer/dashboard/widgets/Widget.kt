package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDisplaySpec
import kotlinx.html.HtmlBlockTag

interface Widget {
    val displaySpec: WidgetDisplaySpec

    fun prepareRender() = Unit
    fun renderInto(target: HtmlBlockTag)
}
