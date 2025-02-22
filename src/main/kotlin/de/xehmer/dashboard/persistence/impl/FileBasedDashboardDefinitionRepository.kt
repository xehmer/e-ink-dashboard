package de.xehmer.dashboard.persistence.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.xehmer.dashboard.api.models.DashboardDefinition
import de.xehmer.dashboard.persistence.DashboardDefinitionRepository
import org.springframework.stereotype.Service
import java.io.File

private const val FILENAME = "dashboard-definition.json"

@Service
class FileBasedDashboardDefinitionRepository(private val objectMapper: ObjectMapper) : DashboardDefinitionRepository {

    override fun loadDashboardDefinition(): DashboardDefinition? {
        val file = File(FILENAME)
        return if (file.canRead()) objectMapper.readValue<DashboardDefinition>(file) else null
    }

    override fun saveDashboardDefinition(dashboardDefinition: DashboardDefinition): Boolean {
        val file = File(FILENAME)
        return file.canWrite().also { objectMapper.writeValue(file, dashboardDefinition) }
    }
}
