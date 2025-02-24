package de.xehmer.dashboard.api.internal

import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Component

/**
 * Jackson [com.fasterxml.jackson.databind.Module] that adds our custom [SubtypeRegisteringAnnotationIntrospector]
 * into the chain of annotation introspectors.
 *
 * Auto-detected by Spring and injected into all Spring-managed [com.fasterxml.jackson.databind.ObjectMapper]s.
 */
@Component
class DashboardAPIJacksonModule(private val subtypeRegisteringAnnotationIntrospector: AnnotationIntrospector) :
    SimpleModule() {

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.insertAnnotationIntrospector(subtypeRegisteringAnnotationIntrospector)
    }

}
