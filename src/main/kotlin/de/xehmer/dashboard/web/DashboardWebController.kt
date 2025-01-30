package de.xehmer.dashboard.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.xehmer.dashboard.api.controllers.DashboardController
import de.xehmer.dashboard.api.models.CalendarWidgetSpec
import de.xehmer.dashboard.api.models.DashboardSpec
import de.xehmer.dashboard.api.models.WeatherWidgetSpec
import de.xehmer.dashboard.api.models.WidgetDisplaySpec
import de.xehmer.dashboard.renderer.DashboardRenderer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class DashboardWebController(
    private val renderer: DashboardRenderer,
    private val objectMapper: ObjectMapper
) : DashboardController {

    override fun getDashboard(spec: String): ResponseEntity<String> {
//        val parsedSpec = objectMapper.readValue<DashboardSpec>(spec)
//        val html = renderer.renderDashboard(parsedSpec)
        val html = renderer.renderDashboard(createDemoDashboardSpec())
        return ResponseEntity(html, HttpStatus.OK)
    }

    private fun createDemoDashboardSpec() = DashboardSpec(
        displayWidth = 1000, displayHeight = 500,
        gridColumnCount = 10, gridRowCount = 5,
        widgets = listOf(
            WeatherWidgetSpec(
                WidgetDisplaySpec(
                    startColumn = 8,
                    startRow = 2,
                    columnSpan = 3,
                    rowSpan = 2,
                    align = "center",
                    justify = "center"
                ),
                lat = 50.91F, lon = 11.56F
            ),
            CalendarWidgetSpec(
                WidgetDisplaySpec(startColumn = 1, startRow = 1, columnSpan = 1, rowSpan = 1),
                calendarId = "test"
            )
        )
    )
}
