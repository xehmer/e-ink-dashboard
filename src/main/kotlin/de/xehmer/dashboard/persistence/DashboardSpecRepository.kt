package de.xehmer.dashboard.persistence

import de.xehmer.dashboard.api.models.DashboardSpec

interface DashboardSpecRepository {
    fun loadDashboardSpec(): DashboardSpec?
    fun saveDashboardSpec(dashboardSpec: DashboardSpec): Boolean
}
