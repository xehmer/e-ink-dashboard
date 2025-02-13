package de.xehmer.dashboard.api

import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Component

@Component
class DashboardAPIJacksonModule(private val subtypeRegisteringAnnotationIntrospector: AnnotationIntrospector) :
    SimpleModule() {

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.insertAnnotationIntrospector(subtypeRegisteringAnnotationIntrospector)
    }

}
