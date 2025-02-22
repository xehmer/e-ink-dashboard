package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.html.HtmlBlockTag
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.p
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

class ErrorWidgetData(val errorMessage: String) {
    constructor(e: Throwable) : this(e.message.toString())
}

@Service
class ErrorWidgetRenderer : WidgetRenderer<WidgetDefinition, ErrorWidgetData> {
    override fun render(
        widget: Widget<WidgetDefinition, ErrorWidgetData>,
        target: HtmlBlockTag
    ) = with(target) {
        div {
            classes = setOf("widget-error")
            inlineStyle {
                overflowWrap = OverflowWrap.anywhere
                fontFamily = "monospace"
                fontSize = 0.5.rem
            }

            p { +"Could not render widget definition ${widget.definition}" }
            p { +"Error message: ${widget.data.errorMessage}" }
        }
    }
}

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
class FallbackWidgetRenderer : WidgetRenderer<WidgetDefinition, Any> {
    override fun render(widget: Widget<WidgetDefinition, Any>, target: HtmlBlockTag) = with(target) {
        div {
            classes = setOf("widget-unknown")
            inlineStyle {
                overflowWrap = OverflowWrap.anywhere
                fontFamily = "monospace"
                fontSize = 0.5.rem
            }

            +"No renderer defined for widget definition [${widget.definition}] and data [${widget.data}]"
        }
    }
}
