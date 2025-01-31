package de.xehmer.dashboard.widgets

import de.xehmer.dashboard.api.models.WidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.utils.inlineStyle
import kotlinx.css.*
import kotlinx.html.HtmlBlockTag
import kotlinx.html.classes
import kotlinx.html.div
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class WidgetTypeRegistry {

    private val specTypeToConstructor1Map: MutableMap<KClass<out WidgetSpec>, (WidgetSpec, DashboardContext) -> Widget> =
        mutableMapOf()
    private val specTypeToConstructor2Map: MutableMap<KClass<out WidgetSpec>, (WidgetSpec, DashboardContext, WidgetController<*, *>) -> Widget> =
        mutableMapOf()
    private val specTypeToControllerMap: MutableMap<KClass<out WidgetSpec>, WidgetController<*, *>> = mutableMapOf()

    fun <S : WidgetSpec, D> registerWidgetType(
        specClass: KClass<S>,
        controller: WidgetController<S, D>,
        constructor: (S, DashboardContext, WidgetController<S, D>) -> Widget
    ) {
        specTypeToControllerMap[specClass] = controller
        specTypeToConstructor2Map[specClass] =
            { constructorSpecParam, constructorContextParam, constructorControllerParam ->
                @Suppress("UNCHECKED_CAST")
                constructor.invoke(
                    constructorSpecParam as S,
                    constructorContextParam,
                    constructorControllerParam as WidgetController<S, D>
                )
            }
    }

    fun <S : WidgetSpec> registerWidgetType(
        specClass: KClass<S>,
        constructor: (S, DashboardContext) -> Widget
    ) {
        specTypeToConstructor1Map[specClass] = { spec, context ->
            @Suppress("UNCHECKED_CAST")
            constructor.invoke(spec as S, context)
        }
    }

    fun createWidget(spec: WidgetSpec, context: DashboardContext): Widget {
        val controller = specTypeToControllerMap[spec::class]
        return if (controller != null) {
            specTypeToConstructor2Map.getValue(spec::class).invoke(spec, context, controller)
        } else {
            specTypeToConstructor1Map.getOrDefault(spec::class, ::ErrorWidget).invoke(spec, context)
        }
    }

    private class ErrorWidget(spec: WidgetSpec, context: DashboardContext) :
        BaseWidgetWithoutController<WidgetSpec>(spec, context) {

        override fun renderInto(target: HtmlBlockTag) = with(target) {
            div {
                classes = setOf("widget-error")
                inlineStyle {
                    overflowWrap = OverflowWrap.anywhere
                    fontFamily = "monospace"
                    fontSize = 0.5.rem
                }

                +"Faulty/invalid widget spec: $spec"
            }
        }
    }
}
