package de.xehmer.dashboard.jenah

import de.schildbach.pte.VmtProvider
import de.schildbach.pte.dto.Departure
import de.schildbach.pte.dto.Line
import de.schildbach.pte.dto.LocationType
import de.schildbach.pte.dto.Product
import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.dashboard.DashboardContext
import de.xehmer.dashboard.widgets.WidgetController
import de.xehmer.dashboard.widgets.WidgetTypeRegistry
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import org.springframework.stereotype.Service
import java.util.*

private const val UNKNOWN = "unknown"

@Service
class JeNahWidgetController(
    widgetTypeRegistry: WidgetTypeRegistry
) : WidgetController<JeNahWidgetSpec, JeNahWidgetData> {

    init {
        @Suppress("LeakingThis")
        widgetTypeRegistry.registerWidgetType(JeNahWidgetSpec::class, this, ::JeNahWidget)
    }

    override fun getData(spec: JeNahWidgetSpec, context: DashboardContext): JeNahWidgetData? {
        val vmtProvider = VmtProvider(spec.apiClient, spec.apiAuthorization)

        val suggestLocationsResult = vmtProvider.suggestLocations(spec.station, setOf(LocationType.STATION), 1)
        val location = suggestLocationsResult.locations[0]

        val queryDeparturesResult = vmtProvider.queryDepartures(location.id, Date(), 5, true)

        val mappedDepartures = queryDeparturesResult.stationDepartures.flatMap { it.departures }
            .map { createDeparture(it, context.timezone) }

        return JeNahWidgetData(mappedDepartures)
    }

    private fun createDeparture(pteDeparture: Departure, timeZone: TimeZone): JeNahWidgetData.Departure {
        return JeNahWidgetData.Departure(
            line = createLine(pteDeparture.line),
            destination = pteDeparture.destination?.name ?: UNKNOWN,
            plannedTime = parseDate(pteDeparture.nullSafePlannedTime(), timeZone),
            predictedTime = parseDate(pteDeparture.nullSafePredictedTime(), timeZone)
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

    private fun parseDate(pteDate: Date, timeZone: TimeZone): LocalDateTime =
        pteDate.toInstant().toKotlinInstant().toLocalDateTime(timeZone)

    private fun Departure.nullSafePlannedTime() = this.plannedTime ?: this.predictedTime!!
    private fun Departure.nullSafePredictedTime() = this.predictedTime ?: this.plannedTime!!

}
