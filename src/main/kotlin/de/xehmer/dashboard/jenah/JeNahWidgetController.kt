package de.xehmer.dashboard.jenah

import de.schildbach.pte.VmtProvider
import de.schildbach.pte.dto.LocationType
import de.xehmer.dashboard.api.models.JeNahWidgetSpec
import de.xehmer.dashboard.widgets.WidgetController
import de.xehmer.dashboard.widgets.WidgetTypeRegistry
import org.springframework.stereotype.Service
import java.util.*

@Service
class JeNahWidgetController(
    widgetTypeRegistry: WidgetTypeRegistry
) : WidgetController<JeNahWidgetSpec, JeNahWidgetData> {

    init {
        @Suppress("LeakingThis")
        widgetTypeRegistry.registerWidgetType(JeNahWidgetSpec::class, this, ::JeNahWidget)
    }

    override fun getData(spec: JeNahWidgetSpec): JeNahWidgetData? {
        val vmtProvider = VmtProvider(spec.apiClient, spec.apiAuthorization)
        val suggestLocationsResult = vmtProvider.suggestLocations(spec.station, setOf(LocationType.STATION), 1)
        val location = suggestLocationsResult.locations[0]
        val result = vmtProvider.queryDepartures(location.id, Date(), 5, true)
        return JeNahWidgetData(result.stationDepartures)
    }
}
