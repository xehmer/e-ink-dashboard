package de.xehmer.dashboard.core.widget.internal

import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.Widget
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import de.xehmer.dashboard.utils.KotlinUtils
import org.springframework.stereotype.Service

@Service
class WidgetPreparationService(private val widgetDataProviders: List<WidgetDataProvider<*, *>>) {
    fun prepareWidget(widgetDefinition: WidgetDefinition, context: DashboardContext): Widget<*, *> {
        for (provider in widgetDataProviders) {
            val definitionClass = KotlinUtils.getSupertypeTypeArgument(provider, WidgetDataProvider::class, 0)
            if (definitionClass.isInstance(widgetDefinition)) {
                @Suppress("UNCHECKED_CAST")
                val data = (provider as WidgetDataProvider<WidgetDefinition, Any>).getData(widgetDefinition, context)
                return Widget(widgetDefinition, data)
            }
        }

        return Widget(widgetDefinition, Unit)
    }
}
