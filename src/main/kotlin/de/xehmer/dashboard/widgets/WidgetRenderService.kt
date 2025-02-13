package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.utils.KotlinUtils
import kotlinx.html.HtmlBlockTag
import org.springframework.stereotype.Service

@Service
class WidgetRenderService(private val widgetRenderers: List<WidgetRenderer<*, *>>) {

    fun renderWidget(widget: PreparedWidget<*, *>, target: HtmlBlockTag) {
        for (renderer in widgetRenderers) {
            val definitionClass = KotlinUtils.getSupertypeTypeArgument(renderer, WidgetRenderer::class, 0)
            val dataClass = KotlinUtils.getSupertypeTypeArgument(renderer, WidgetRenderer::class, 1)
            if (definitionClass.isInstance(widget.definition) && dataClass.isInstance(widget.data)) {
                renderer.renderWithUncheckedCast(widget, target)
                return
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <S : WidgetDefinition, D : Any> WidgetRenderer<S, D>.renderWithUncheckedCast(
        widget: PreparedWidget<*, *>,
        target: HtmlBlockTag
    ) {
        render(widget as PreparedWidget<S, D>, target)
    }
}
