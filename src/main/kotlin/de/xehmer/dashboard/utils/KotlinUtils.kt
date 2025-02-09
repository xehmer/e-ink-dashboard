package de.xehmer.dashboard.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

object KotlinUtils {
    fun getSupertypeTypeArgument(obj: Any, supertypeClassToFind: KClass<*>, typeArgumentIndex: Int): KClass<*> {
        return obj::class.allSupertypes
            .single { it.classifier == supertypeClassToFind }
            .arguments[typeArgumentIndex]
            .type!!.classifier as KClass<*>
    }
}
