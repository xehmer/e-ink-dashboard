package de.xehmer.dashboard.renderer

import de.xehmer.dashboard.api.models.DashboardDefinition
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.utils.inlineStyle
import de.xehmer.dashboard.utils.repeat
import de.xehmer.dashboard.widgets.*
import kotlinx.css.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.StructuredTaskScope
import kotlin.time.Duration.Companion.seconds

@Service
class DashboardRenderer(
    private val widgetRenderService: WidgetRenderService,
    private val widgetPreparationService: WidgetPreparationService,
) {
    fun renderDashboard(dashboardDefinition: DashboardDefinition): String {
        val unpreparedWidgets = createWidgets(dashboardDefinition)
        val preparedWidgets = prepareWidgets(unpreparedWidgets)
        return buildHTML(dashboardDefinition, preparedWidgets)
    }

    private fun createWidgets(dashboardDefinition: DashboardDefinition): List<UnpreparedWidget<*>> {
        val dashboardContext = DashboardContext(
            timezone = TimeZone.of(dashboardDefinition.context.timeZone),
            locale = Locale.of(dashboardDefinition.context.locale)
        )
        return dashboardDefinition.widgets.map { widgetDefinition ->
            UnpreparedWidget(widgetDefinition, dashboardContext)
        }
    }

    private fun prepareWidgets(widgets: List<UnpreparedWidget<*>>): List<PreparedWidget<*, *>> {
        StructuredTaskScope<PreparedWidget<*, *>>().use { taskScope ->
            val subtasksMap = widgets.associateWith { taskScope.fork { widgetPreparationService.prepareWidget(it) } }

            taskScope.joinUntil(Clock.System.now().plus(5.seconds).toJavaInstant())
            taskScope.shutdown()

            val result = mutableListOf<PreparedWidget<*, *>>()
            for ((widget, subtask) in subtasksMap) {
                result += when (subtask.state()) {
                    StructuredTaskScope.Subtask.State.SUCCESS -> subtask.get()!!

                    StructuredTaskScope.Subtask.State.FAILED -> PreparedWidget(
                        widget.definition,
                        widget.context,
                        ErrorWidgetData(subtask.exception())
                    )

                    else -> PreparedWidget(
                        widget.definition,
                        widget.context,
                        ErrorWidgetData("Unknown error during widget preparation")
                    )
                }
            }
            return result
        }
    }

    private fun buildHTML(dashboardDefinition: DashboardDefinition, widgets: List<PreparedWidget<*, *>>) =
        createHTML(prettyPrint = false).html {
            inlineStyle {
                fontSize = 1.25.rem
                fontFamily = "sans-serif"
            }

            body {
                inlineStyle {
                    margin = Margin(0.pt)
                    padding = Padding(0.pt)
                    width = dashboardDefinition.display.width.px
                    height = dashboardDefinition.display.height.px
                }

                div {
                    id = "grid-container"
                    inlineStyle {
                        margin = Margin(0.25.rem)
                        width = 100.pct - 0.5.rem
                        height = 100.pct - 0.5.rem
                        display = Display.grid
                        gridTemplateColumns = GridTemplateColumns.repeat(dashboardDefinition.display.columnCount, 1.fr)
                        gridTemplateRows = GridTemplateRows.repeat(dashboardDefinition.display.rowCount, 1.fr)
                        gap = 0.125.rem
                    }

                    for ((index, widget) in widgets.withIndex()) {
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

                            widgetRenderService.renderWidget(widget, this)
                        }
                    }
                }
            }
        }
}
