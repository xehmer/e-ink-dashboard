package de.xehmer.dashboard.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.xehmer.dashboard.api.models.DashboardSpec
import org.springframework.stereotype.Service
import java.io.File

private const val FILENAME = "dashboard-spec.json"

@Service
class FileBasedDashboardSpecRepository(private val objectMapper: ObjectMapper) : DashboardSpecRepository {

    override fun loadDashboardSpec(): DashboardSpec? {
        val file = File(FILENAME)
        return if (file.canRead()) objectMapper.readValue<DashboardSpec>(file) else null
    }

    override fun saveDashboardSpec(dashboardSpec: DashboardSpec): Boolean {
        val file = File(FILENAME)
        return file.canWrite().also { objectMapper.writeValue(file, dashboardSpec) }
    }
}
