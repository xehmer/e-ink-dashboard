package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext

abstract class WidgetWithoutData<S : WidgetSpec>(
    protected val spec: S,
    protected val context: DashboardContext
) : Widget {
    override val displaySpec
        get() = spec.display
}
