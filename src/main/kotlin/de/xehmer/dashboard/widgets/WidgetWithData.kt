package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import kotlinx.html.HtmlBlockTag

abstract class WidgetWithData<S : WidgetSpec, D>(
    spec: S,
    context: DashboardContext,
    private val controller: WidgetController<S, D>
) : WidgetWithoutData<S>(spec, context) {

    private var preparedData: D? = null

    final override fun prepareRender() {
        preparedData = controller.getData(spec, context)
    }

    final override fun renderInto(target: HtmlBlockTag) {
        val data = preparedData ?: throw IllegalStateException("Did not call prepareRender() beforehand")
        renderInto(target, data)
    }

    protected abstract fun renderInto(target: HtmlBlockTag, data: D)
}
