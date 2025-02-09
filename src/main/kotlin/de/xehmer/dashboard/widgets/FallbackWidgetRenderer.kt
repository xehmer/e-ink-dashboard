package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.html.HtmlBlockTag
import kotlinx.html.classes
import kotlinx.html.div
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
class FallbackWidgetRenderer : WidgetRenderer<WidgetSpec, Any> {
    override fun render(widget: PreparedWidget<WidgetSpec, Any>, target: HtmlBlockTag) = with(target) {
        div {
            classes = setOf("widget-unknown")
            inlineStyle {
                overflowWrap = OverflowWrap.anywhere
                fontFamily = "monospace"
                fontSize = 0.5.rem
            }

            +"No renderer defined for spec [${widget.spec}] and data [${widget.data}]"
        }
    }
}
