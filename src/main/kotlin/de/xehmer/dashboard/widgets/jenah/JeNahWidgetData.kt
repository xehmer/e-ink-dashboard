package de.xehmer.dashboard.widgets.jenah

import de.schildbach.pte.VmtProvider
import de.schildbach.pte.dto.Departure
import de.schildbach.pte.dto.Line
import de.schildbach.pte.dto.LocationType
import de.schildbach.pte.dto.Product
import de.xehmer.dashboard.api.ApiModule
import de.xehmer.dashboard.core.dashboard.DashboardContext
import de.xehmer.dashboard.core.widget.WidgetDataProvider
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

data class JeNahWidgetData(
    val departures: List<Departure>
) {
    data class Departure(
        val destination: String,
        val line: Line,
        val plannedTime: LocalDateTime,
        val predictedTime: LocalDateTime,
        val delay: Duration
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

private const val UNKNOWN = "unknown"

@Service
class JeNahWidgetDataProvider(apiModule: ApiModule) : WidgetDataProvider<JeNahWidgetDefinition, JeNahWidgetData> {

    init {
        apiModule.registerWidgetDefinition(JeNahWidgetDefinition::class)
    }

    override fun getData(widgetDefinition: JeNahWidgetDefinition, context: DashboardContext): JeNahWidgetData {
        val vmtProvider = VmtProvider(widgetDefinition.apiClient, widgetDefinition.apiAuthorization)

        val suggestLocationsResult = vmtProvider.suggestLocations(
            widgetDefinition.station, setOf(LocationType.STATION), 1
        )
        val location = suggestLocationsResult.locations[0]

        val queryDeparturesResult = vmtProvider.queryDepartures(location.id, Date(), 10, true)

        val mappedDepartures = queryDeparturesResult.stationDepartures.flatMap { it.departures }
            .map { createDeparture(it, context.timezone) }
            .sortedBy { it.predictedTime }

        return JeNahWidgetData(mappedDepartures)
    }

    private fun createDeparture(pteDeparture: Departure, timeZone: ZoneId): JeNahWidgetData.Departure {
        val plannedTime = pteDeparture.nullSafePlannedTime()
        val predictedTime = pteDeparture.nullSafePredictedTime()
        val delay = Duration.between(plannedTime, predictedTime)

        return JeNahWidgetData.Departure(
            line = createLine(pteDeparture.line),
            destination = pteDeparture.destination?.name ?: UNKNOWN,
            plannedTime = LocalDateTime.ofInstant(plannedTime, timeZone),
            predictedTime = LocalDateTime.ofInstant(predictedTime, timeZone),
            delay = delay
        )
    }

    private fun createLine(pteLine: Line): JeNahWidgetData.Line {
        return JeNahWidgetData.Line(
            type = mapPteProduct(pteLine.product),
            number = pteLine.label?.toIntOrNull(),
            displayString = pteLine.name ?: UNKNOWN
        )
    }

    private fun mapPteProduct(pteProduct: Product?): JeNahWidgetData.TransportType = when (pteProduct) {
        Product.BUS -> JeNahWidgetData.TransportType.BUS
        Product.TRAM -> JeNahWidgetData.TransportType.TRAM
        else -> JeNahWidgetData.TransportType.UNKNOWN
    }

    private fun Departure.nullSafePlannedTime() = (this.plannedTime ?: this.predictedTime!!).toInstant()

    private fun Departure.nullSafePredictedTime() = (this.predictedTime ?: this.plannedTime!!).toInstant()
}
