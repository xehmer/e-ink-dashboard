package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext

abstract class BaseWidget<S : WidgetSpec, D>(
    protected val spec: S,
    protected val context: DashboardContext,
    private val controller: WidgetController<S, D>? = null,
) : Widget {

    override val displaySpec
        get() = spec.display

    protected var preparedData: D? = null

    override fun prepareRender() {
        preparedData = controller?.getData(spec, context)
    }
}

abstract class BaseWidgetWithoutController<S : WidgetSpec>(spec: S, context: DashboardContext) :
    BaseWidget<S, Nothing>(spec, context)
