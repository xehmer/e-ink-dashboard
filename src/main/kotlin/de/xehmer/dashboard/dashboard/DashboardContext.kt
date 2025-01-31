package de.xehmer.dashboard.dashboard

import kotlinx.datetime.TimeZone
import java.util.*

data class DashboardContext(
    val timezone: TimeZone,
    val locale: Locale,
)
