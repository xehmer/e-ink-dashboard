package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec

abstract class BaseWidget<S : WidgetSpec, D : WidgetData>(
    protected val spec: S,
    private val controller: WidgetController<S, D>,
) : Widget {

    override val displaySpec
        get() = spec.display

    protected var data: D? = null

    override fun prepareRender() {
        data = controller.getData(spec)
    }
}
