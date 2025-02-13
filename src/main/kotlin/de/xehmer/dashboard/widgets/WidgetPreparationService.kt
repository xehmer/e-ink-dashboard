package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetDefinition
import de.xehmer.dashboard.utils.KotlinUtils
import org.springframework.stereotype.Service

@Service
class WidgetPreparationService(private val widgetDataProviders: List<WidgetDataProvider<*, *>>) {
    fun prepareWidget(widget: UnpreparedWidget<*>): PreparedWidget<*, *> {
        for (provider in widgetDataProviders) {
            val definitionClass = KotlinUtils.getSupertypeTypeArgument(provider, WidgetDataProvider::class, 0)
            if (definitionClass.isInstance(widget.definition)) {
                val data = provider.getDataWithUncheckedCast(widget)
                return PreparedWidget(widget.definition, widget.context, data)
            }
        }

        return PreparedWidget(widget.definition, widget.context, Unit)
    }

    @Suppress("UNCHECKED_CAST")
    fun <S : WidgetDefinition, D : Any> WidgetDataProvider<S, D>.getDataWithUncheckedCast(widget: UnpreparedWidget<*>): D {
        return this.getData(widget as UnpreparedWidget<S>)
    }
}
