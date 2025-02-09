package de.xehmer.dashboard.jenah

import de.schildbach.pte.VmtProvider
import de.schildbach.pte.dto.Departure
import de.schildbach.pte.dto.Line
import de.schildbach.pte.dto.LocationType
import de.schildbach.pte.dto.Product
import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.widgets.UnpreparedWidget
import de.xehmer.dashboard.widgets.WidgetDataProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import org.springframework.stereotype.Service
import java.util.*

private const val UNKNOWN = "unknown"

@Service
class JeNahWidgetDataProvider : WidgetDataProvider<JeNahWidgetSpec, JeNahWidgetData> {

    override fun getData(widget: UnpreparedWidget<JeNahWidgetSpec>): JeNahWidgetData {
        val vmtProvider = VmtProvider(widget.spec.apiClient, widget.spec.apiAuthorization)

        val suggestLocationsResult = vmtProvider.suggestLocations(widget.spec.station, setOf(LocationType.STATION), 1)
        val location = suggestLocationsResult.locations[0]

        val queryDeparturesResult = vmtProvider.queryDepartures(location.id, Date(), 10, true)

        val mappedDepartures = queryDeparturesResult.stationDepartures.flatMap { it.departures }
            .map { createDeparture(it, widget.context.timezone) }
            .sortedBy { it.predictedTime }

        return JeNahWidgetData(mappedDepartures)
    }

    private fun createDeparture(pteDeparture: Departure, timeZone: TimeZone): JeNahWidgetData.Departure {
        val plannedTime = pteDeparture.nullSafePlannedTime()
        val predictedTime = pteDeparture.nullSafePredictedTime()
        val delay = predictedTime.minus(plannedTime)

        return JeNahWidgetData.Departure(
            line = createLine(pteDeparture.line),
            destination = pteDeparture.destination?.name ?: UNKNOWN,
            plannedTime = plannedTime.toLocalDateTime(timeZone),
            predictedTime = predictedTime.toLocalDateTime(timeZone),
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

    private fun Departure.nullSafePlannedTime() =
        (this.plannedTime ?: this.predictedTime!!).toInstant().toKotlinInstant()

    private fun Departure.nullSafePredictedTime() =
        (this.predictedTime ?: this.plannedTime!!).toInstant().toKotlinInstant()
}
