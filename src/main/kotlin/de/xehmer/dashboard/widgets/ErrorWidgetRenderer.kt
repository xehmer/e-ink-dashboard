package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
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
class ErrorWidgetRenderer : WidgetRenderer<WidgetSpec, ErrorWidgetData> {
    override fun render(widget: PreparedWidget<WidgetSpec, ErrorWidgetData>, target: HtmlBlockTag) = with(target) {
        div {
            classes = setOf("widget-error")
            inlineStyle {
                overflowWrap = OverflowWrap.anywhere
                fontFamily = "monospace"
                fontSize = 0.5.rem
            }

            p { +"Faulty/invalid widget spec: ${widget.spec}" }
            p { +"Error message: ${widget.data.errorMessage}" }

        }
    }
}
