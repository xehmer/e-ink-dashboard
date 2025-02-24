package de.xehmer.dashboard.persistence

import de.xehmer.dashboard.api.DashboardDefinition

interface DashboardDefinitionRepository {
    fun loadDashboardDefinition(): DashboardDefinition?
    fun saveDashboardDefinition(dashboardDefinition: DashboardDefinition): Boolean
}
