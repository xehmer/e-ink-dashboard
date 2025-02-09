package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.utils.KotlinUtils
import org.springframework.stereotype.Service

@Service
class WidgetPreparationService(private val widgetDataProviders: List<WidgetDataProvider<*, *>>) {
    fun prepareWidget(widget: UnpreparedWidget<*>): PreparedWidget<*, *> {
        for (provider in widgetDataProviders) {
            val specClass = KotlinUtils.getSupertypeTypeArgument(provider, WidgetDataProvider::class, 0)
            if (specClass.isInstance(widget.spec)) {
                val data = provider.getDataWithUncheckedCast(widget)
                return PreparedWidget(widget.spec, widget.context, data)
            }
        }

        return PreparedWidget(widget.spec, widget.context, Unit)
    }

    @Suppress("UNCHECKED_CAST")
    fun <S : WidgetSpec, D : Any> WidgetDataProvider<S, D>.getDataWithUncheckedCast(widget: UnpreparedWidget<*>): D {
        return this.getData(widget as UnpreparedWidget<S>)
    }
}
