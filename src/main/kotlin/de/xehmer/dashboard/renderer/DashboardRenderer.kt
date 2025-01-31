package de.xehmer.dashboard.renderer

import de.xehmer.dashboard.api.models.DashboardSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.utils.inlineStyle
import de.xehmer.dashboard.utils.repeat
import de.xehmer.dashboard.widgets.Widget
import de.xehmer.dashboard.widgets.WidgetTypeRegistry
import kotlinx.css.*
import kotlinx.datetime.TimeZone
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.StructuredTaskScope

@Service
class DashboardRenderer(private val widgetTypeRegistry: WidgetTypeRegistry) {

    fun renderDashboard(dashboardSpec: DashboardSpec): String {
        val widgets = createWidgets(dashboardSpec)
        prepareWidgets(widgets)
        return buildHTML(dashboardSpec, widgets)
    }

    private fun createWidgets(dashboardSpec: DashboardSpec): List<Widget> {
        val dashboardContext = DashboardContext(
            timezone = TimeZone.of(dashboardSpec.context.timeZone),
            locale = Locale.of(dashboardSpec.context.locale)
        )
        return dashboardSpec.widgets.map { widgetTypeRegistry.createWidget(it, dashboardContext) }
    }

    private fun prepareWidgets(widgets: List<Widget>) {
        StructuredTaskScope.ShutdownOnFailure().use { taskScope ->
            widgets.forEach { taskScope.fork { it.prepareRender() } }
            taskScope.join()
            taskScope.throwIfFailed()
        }
    }

    private fun buildHTML(dashboardSpec: DashboardSpec, widgets: List<Widget>) =
        createHTML(prettyPrint = false).html {
            inlineStyle {
                fontSize = 1.25.rem
                fontFamily = "sans-serif"
            }

            body {
                inlineStyle {
                    margin = Margin(0.pt)
                    padding = Padding(0.pt)
                    width = dashboardSpec.display.width.px
                    height = dashboardSpec.display.height.px
                }

                div {
                    id = "grid-container"
                    inlineStyle {
                        margin = Margin(0.25.rem)
                        width = 100.pct - 0.5.rem
                        height = 100.pct - 0.5.rem
                        display = Display.grid
                        gridTemplateColumns = GridTemplateColumns.repeat(dashboardSpec.display.columnCount, 1.fr)
                        gridTemplateRows = GridTemplateRows.repeat(dashboardSpec.display.rowCount, 1.fr)
                        gap = 0.125.rem
                    }

                    for ((index, widget) in widgets.withIndex()) {
                        div {
                            id = "grid-item-$index"
                            inlineStyle {
                                val displaySpec = widget.displaySpec
                                gridColumn = GridColumn("${displaySpec.startColumn} / span ${displaySpec.columnSpan}")
                                gridRow = GridRow("${displaySpec.startRow} / span ${displaySpec.rowSpan}")
                                overflow = Overflow.hidden
                                displaySpec.align?.let { alignSelf = Align.valueOf(it) }
                                displaySpec.justify?.let { justifySelf = JustifySelf.valueOf(it) }
                            }

                            widget.renderInto(this)
                        }
                    }
                }
            }
        }
}
