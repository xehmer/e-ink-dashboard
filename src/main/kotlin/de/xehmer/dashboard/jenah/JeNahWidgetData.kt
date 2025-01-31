package de.xehmer.dashboard.jenah

import kotlinx.datetime.LocalDateTime

data class JeNahWidgetData(
    val departures: List<Departure>
) {
    data class Departure(
        val destination: String,
        val line: Line,
        val plannedTime: LocalDateTime,
        val predictedTime: LocalDateTime
    )

    data class Line(
        val type: TransportType,
        val number: Int?,
        val displayString: String
    )

    enum class TransportType {
        BUS, TRAM, UNKNOWN
    }
}
