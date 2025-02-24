package de.xehmer.dashboard.base

import de.xehmer.dashboard.api.DashboardDefinition
import kotlinx.datetime.TimeZone
import org.springframework.stereotype.Service
import java.util.*

@Service
class DashboardFactory {
    fun createDashboard(definition: DashboardDefinition): UnpreparedDashboard {
        val dashboardContext = DashboardContext(
            timezone = TimeZone.of(definition.context.timeZone),
            locale = Locale.of(definition.context.locale)
        )
        return UnpreparedDashboard(
            display = definition.display,
            context = dashboardContext,
            widgets = definition.widgets
        )
    }
}
