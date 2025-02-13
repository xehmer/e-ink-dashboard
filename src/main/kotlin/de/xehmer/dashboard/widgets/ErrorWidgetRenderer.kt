package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.html.HtmlBlockTag
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.p
import org.springframework.stereotype.Service

class ErrorWidgetData(val errorMessage: String) {
    constructor(e: Throwable) : this(e.message.toString())
}

@Service
class ErrorWidgetRenderer : WidgetRenderer<WidgetDefinition, ErrorWidgetData> {
    override fun render(
        widget: PreparedWidget<WidgetDefinition, ErrorWidgetData>,
        target: HtmlBlockTag
    ) = with(target) {
        div {
            classes = setOf("widget-error")
            inlineStyle {
                overflowWrap = OverflowWrap.anywhere
                fontFamily = "monospace"
                fontSize = 0.5.rem
            }

            p { +"Faulty/invalid widget definition: ${widget.definition}" }
            p { +"Error message: ${widget.data.errorMessage}" }

        }
    }
}
