package de.xehmer.dashboard.core.dashboard

import de.xehmer.dashboard.api.DashboardDefinition
import de.xehmer.dashboard.core.dashboard.internal.DashboardFactory
import de.xehmer.dashboard.core.dashboard.internal.DashboardPreparationService
import de.xehmer.dashboard.core.widget.internal.WidgetRenderService
import de.xehmer.dashboard.utils.inlineStyle
import de.xehmer.dashboard.utils.repeat
import kotlinx.css.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.stream.createHTML
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
        return buildHTML(preparedDashboard)
    }

    private fun buildHTML(dashboard: PreparedDashboard): String {
        val dashboardDisplay = dashboard.display
        return createHTML(prettyPrint = false).html {
            inlineStyle {
                fontSize = 1.25.rem
                fontFamily = "sans-serif"
            }

            body {
                inlineStyle {
                    margin = Margin(0.pt)
                    padding = Padding(0.pt)
                    width = dashboardDisplay.width.px
                    height = dashboardDisplay.height.px
                }

                div {
                    id = "grid-container"
                    inlineStyle {
                        margin = Margin(0.25.rem)
                        width = 100.pct - 0.5.rem
                        height = 100.pct - 0.5.rem
                        display = Display.grid
                        gridTemplateColumns = GridTemplateColumns.repeat(dashboardDisplay.columnCount, 1.fr)
                        gridTemplateRows = GridTemplateRows.repeat(dashboardDisplay.rowCount, 1.fr)
                        gap = 0.125.rem
                    }

                    for ((index, widget) in dashboard.widgets.withIndex()) {
                        div {
                            id = "grid-item-$index"
                            inlineStyle {
                                val displayDefinition = widget.definition.display
                                gridColumn =
                                    GridColumn("${displayDefinition.startColumn} / span ${displayDefinition.columnSpan}")
                                gridRow = GridRow("${displayDefinition.startRow} / span ${displayDefinition.rowSpan}")
                                overflow = Overflow.hidden
                                displayDefinition.align?.let { alignSelf = Align.valueOf(it) }
                                displayDefinition.justify?.let { justifySelf = JustifySelf.valueOf(it) }
                            }

                            widgetRenderService.renderWidget(widget, dashboard.context, this)
                        }
                    }
                }
            }
        }
    }
}
