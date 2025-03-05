package de.xehmer.dashboard.core.dashboard

import de.xehmer.dashboard.api.DashboardDefinition
import de.xehmer.dashboard.core.dashboard.internal.DashboardFactory
import de.xehmer.dashboard.core.dashboard.internal.DashboardPreparationService
import de.xehmer.dashboard.core.widget.internal.WidgetRenderService
import de.xehmer.dashboard.utils.inlineStyle
import de.xehmer.dashboard.utils.repeat
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.springframework.stereotype.Service

@Service
class DashboardRenderer(
    private val widgetRenderService: WidgetRenderService,
    private val dashboardFactory: DashboardFactory,
    private val dashboardPreparationService: DashboardPreparationService,
) {
    fun renderDashboard(dashboardDefinition: DashboardDefinition): String {
        val unpreparedDashboard = dashboardFactory.createDashboard(dashboardDefinition)
        val preparedDashboard = dashboardPreparationService.prepareDashboard(unpreparedDashboard)
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().html {
                createHtmlContent(preparedDashboard)
            }
        }
    }

    private fun HTML.createHtmlContent(dashboard: PreparedDashboard) {
        lang = dashboard.context.locale.language

        inlineStyle {
            fontSize = 16.px
            fontFamily = "sans-serif"
        }

        head {
            meta(charset = "utf-8")

            style {
                unsafe {
                    raw(CssBuilder().apply {
                        universal {
                            margin = Margin(0.px)
                            padding = Padding(0.pt)
                        }
                    }.toString())
                }
            }
        }

        body {
            val dashboardDisplay = dashboard.display
            inlineStyle {
                width = dashboardDisplay.width.px
                height = dashboardDisplay.height.px
            }

            div {
                id = "grid-container"
                inlineStyle {
                    val outerMargin = 0.25.rem
                    margin = Margin(outerMargin)
                    width = 100.pct - outerMargin * 2
                    height = 100.pct - outerMargin * 2
                    display = Display.grid
                    gridTemplateColumns = GridTemplateColumns.repeat(dashboardDisplay.columnCount, 1.fr)
                    gridTemplateRows = GridTemplateRows.repeat(dashboardDisplay.rowCount, 1.fr)
                    gap = outerMargin / 2
                }

                for ((index, widget) in dashboard.widgets.withIndex()) {
                    div {
                        id = "grid-item-$index"
                        inlineStyle {
                            val widgetDisplay = widget.definition.display
                            gridColumn = GridColumn("${widgetDisplay.startColumn} / span ${widgetDisplay.columnSpan}")
                            gridRow = GridRow("${widgetDisplay.startRow} / span ${widgetDisplay.rowSpan}")
                            overflow = Overflow.hidden
                            widgetDisplay.align?.let { alignSelf = Align.valueOf(it) }
                            widgetDisplay.justify?.let { justifySelf = JustifySelf.valueOf(it) }
                        }

                        widgetRenderService.renderWidget(widget, dashboard.context, this)
                    }
                }
            }
        }
    }
}
