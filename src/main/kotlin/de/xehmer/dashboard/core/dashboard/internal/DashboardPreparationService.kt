package de.xehmer.dashboard.core.dashboard.internal

import de.xehmer.dashboard.core.dashboard.PreparedDashboard
import de.xehmer.dashboard.core.dashboard.UnpreparedDashboard
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.internal.ErrorWidgetData
import de.xehmer.dashboard.core.widget.internal.WidgetPreparationService
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.StructuredTaskScope

@Service
class DashboardPreparationService(
    private val widgetPreparationService: WidgetPreparationService
) {
    fun prepareDashboard(dashboard: UnpreparedDashboard): PreparedDashboard {
        StructuredTaskScope<Widget<*, *>>().use { taskScope ->
            val subtasksMap = dashboard.widgets.associateWith {
                taskScope.fork { widgetPreparationService.prepareWidget(it, dashboard.context) }
            }

            taskScope.joinUntil(Instant.now().plusSeconds(10))
            taskScope.shutdown()

            val widgets = mutableListOf<Widget<*, *>>()
            for ((widgetDefinition, subtask) in subtasksMap) {
                widgets += when (subtask.state()) {
                    StructuredTaskScope.Subtask.State.SUCCESS -> subtask.get()!!

                    StructuredTaskScope.Subtask.State.FAILED -> Widget(
                        widgetDefinition,
                        ErrorWidgetData(subtask.exception())
                    )

                    else -> Widget(
                        widgetDefinition,
                        ErrorWidgetData("Unknown error during widget preparation")
                    )
                }
            }

            return PreparedDashboard(
                display = dashboard.display,
                context = dashboard.context,
                widgets = widgets
            )
        }
    }
}
