package de.xehmer.dashboard.core.dashboard

import de.xehmer.dashboard.api.DashboardDisplayDefinition
import de.xehmer.dashboard.api.WidgetDefinition
import de.xehmer.dashboard.core.widget.Widget
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class DashboardContext(
    val timezone: ZoneId,
    val locale: Locale,
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale),
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale),
)

data class UnpreparedDashboard(
    val display: DashboardDisplayDefinition,
    val context: DashboardContext,
    val widgets: List<WidgetDefinition>
)

data class PreparedDashboard(
    val display: DashboardDisplayDefinition,
    val context: DashboardContext,
    val widgets: List<Widget<*, *>>
)
