package de.xehmer.dashboard.core.widget

import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import kotlinx.html.HtmlBlockTag
import org.springframework.core.annotation.Order

@Order(0)
fun interface WidgetRenderer<S : WidgetDefinition, D : Any> {
    fun render(widget: Widget<S, D>, context: DashboardContext, target: HtmlBlockTag)
}
