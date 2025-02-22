package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.utils.KotlinUtils
import kotlinx.html.HtmlBlockTag
import org.springframework.stereotype.Service

fun interface WidgetRenderer<S : WidgetDefinition, D : Any> {
    fun render(widget: PreparedWidget<S, D>, target: HtmlBlockTag)
}

@Service
class WidgetRenderService(private val widgetRenderers: List<WidgetRenderer<*, *>>) {
    fun renderWidget(widget: PreparedWidget<*, *>, target: HtmlBlockTag) {
        for (renderer in widgetRenderers) {
            val definitionClass = KotlinUtils.getSupertypeTypeArgument(renderer, WidgetRenderer::class, 0)
            val dataClass = KotlinUtils.getSupertypeTypeArgument(renderer, WidgetRenderer::class, 1)
            if (definitionClass.isInstance(widget.definition) && dataClass.isInstance(widget.data)) {
                @Suppress("UNCHECKED_CAST")
                (renderer as WidgetRenderer<WidgetDefinition, Any>).render(widget, target)
                return
            }
        }
    }
}
