package de.xehmer.dashboard.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.core.util.RefUtils
import io.swagger.v3.oas.models.media.Discriminator
import org.springframework.stereotype.Component
import java.lang.reflect.Type

/**
 * Freely copied from [here](https://github.com/swagger-api/swagger-core/issues/3411#issuecomment-588966676)
 */
@Component
class DiscriminatorMappingEnhancingModelConverter(objectMapper: ObjectMapper) : ModelResolver(objectMapper) {

    override fun resolveDiscriminator(type: JavaType, context: ModelConverterContext): Discriminator? {
        val discriminator: Discriminator? = super.resolveDiscriminator(type, context)
        if (discriminator?.propertyName != null) {
            addResolvedSubTypeMappings(discriminator, type, context)
            addAnnotatedSubTypeMappings(discriminator, type, context)
        }
        return discriminator
    }

    private fun addResolvedSubTypeMappings(
        discriminator: Discriminator,
        type: JavaType,
        context: ModelConverterContext
    ) {
        val config = objectMapper().serializationConfig
        objectMapper().subtypeResolver.collectAndResolveSubtypesByClass(
            config,
            AnnotatedClassResolver.resolveWithoutSuperTypes(config, type, config)
        )
            .stream()
            .filter { namedType -> namedType.type != type.rawClass }
            .forEach { namedType -> addMapping(discriminator, namedType.name, namedType.type, context) }
    }

    private fun addAnnotatedSubTypeMappings(
        discriminator: Discriminator,
        type: JavaType,
        context: ModelConverterContext
    ) {
        val jsonSubTypes = type.rawClass.getDeclaredAnnotation(JsonSubTypes::class.java)
        jsonSubTypes?.value?.forEach { subtype -> addMapping(discriminator, subtype.name, subtype.value.java, context) }
    }

    private fun addMapping(discriminator: Discriminator, name: String?, type: Type, context: ModelConverterContext) {
        val isNamed = !name.isNullOrBlank()
        val schemaName = context.resolve(AnnotatedType().type(type)).name
        val ref = RefUtils.constructRef(schemaName)
        val mappings = discriminator.mapping.orEmpty()

        if (!isNamed && mappings.containsValue(ref)) {
            // Skip adding the unnamed mapping
            return
        }

        discriminator.mapping(if (isNamed) name else schemaName, ref)

        if (isNamed && ref == mappings[schemaName]) {
            // Remove previous unnamed mapping
            discriminator.mapping.remove(schemaName)
        }
    }
}
