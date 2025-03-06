package de.xehmer.dashboard.core.dashboard.internal

import de.xehmer.dashboard.api.DashboardDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.dashboard.UnpreparedDashboard
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.Locale

@Service
class DashboardFactory {
    fun createDashboard(definition: DashboardDefinition): UnpreparedDashboard {
        val locale = Locale.forLanguageTag(definition.context.locale)
        val dashboardContext = DashboardContext(
            timezone = ZoneId.of(definition.context.timeZone),
            locale = locale,
        )
        return UnpreparedDashboard(
            display = definition.display,
            context = dashboardContext,
            widgets = definition.widgets
        )
    }
}
