package de.xehmer.dashboard.core.dashboard.internal

import de.xehmer.dashboard.api.DashboardDefinition
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.dashboard.UnpreparedDashboard
import kotlinx.datetime.TimeZone
import org.springframework.stereotype.Service
import java.util.*

@Service
class DashboardFactory {
    fun createDashboard(definition: DashboardDefinition): UnpreparedDashboard {
        val dashboardContext = DashboardContext(
            timezone = TimeZone.of(definition.context.timeZone),
            locale = Locale.forLanguageTag(definition.context.locale)
        )
        return UnpreparedDashboard(
            display = definition.display,
            context = dashboardContext,
            widgets = definition.widgets
        )
    }
}
