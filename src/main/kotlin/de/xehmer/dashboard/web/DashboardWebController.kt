package de.xehmer.dashboard.web

import de.xehmer.dashboard.api.controllers.DashboardController
import de.xehmer.dashboard.api.controllers.DashboardSpecController
import de.xehmer.dashboard.api.models.DashboardSpec
import de.xehmer.dashboard.persistence.DashboardSpecRepository
import de.xehmer.dashboard.renderer.DashboardRenderer
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class DashboardWebController(
    private val renderer: DashboardRenderer,
    private val dashboardSpecRepository: DashboardSpecRepository,
) : DashboardController, DashboardSpecController {

    override fun getDashboard(): ResponseEntity<String> {
        val spec = dashboardSpecRepository.loadDashboardSpec()
        val html = if (spec == null) {
            createHTML().html { body { +"No saved spec found." } }
        } else {
            renderer.renderDashboard(spec)
        }
        return ResponseEntity.ok(html)
    }

    override fun saveDashboardSpec(dashboardSpec: DashboardSpec): ResponseEntity<Unit> {
        val success = dashboardSpecRepository.saveDashboardSpec(dashboardSpec)
        return if (success) ResponseEntity.noContent().build() else ResponseEntity.internalServerError().build()
    }
}
