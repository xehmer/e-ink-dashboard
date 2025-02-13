package de.xehmer.dashboard.web

import de.xehmer.dashboard.api.models.DashboardSpec
import de.xehmer.dashboard.persistence.DashboardSpecRepository
import de.xehmer.dashboard.renderer.DashboardRenderer
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class DashboardWebController(
    private val renderer: DashboardRenderer,
    private val dashboardSpecRepository: DashboardSpecRepository,
) {

    @GetMapping("/dashboard", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getDashboard(): String {
        val spec = dashboardSpecRepository.loadDashboardSpec()
        val html = if (spec == null) {
            createHTML().html { body { +"No saved spec found." } }
        } else {
            renderer.renderDashboard(spec)
        }
        return html
    }

    @PutMapping("/dashboard/spec", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveDashboardSpec(dashboardSpec: DashboardSpec) {
        dashboardSpecRepository.saveDashboardSpec(dashboardSpec)
    }
}
