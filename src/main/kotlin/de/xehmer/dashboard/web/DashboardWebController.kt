package de.xehmer.dashboard.web

import de.xehmer.dashboard.api.models.DashboardDefinition
import de.xehmer.dashboard.persistence.DashboardDefinitionRepository
import de.xehmer.dashboard.renderer.DashboardRenderer
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class DashboardWebController(
    private val renderer: DashboardRenderer,
    private val dashboardDefinitionRepository: DashboardDefinitionRepository,
) {

    @GetMapping("/dashboard", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getDashboard(): String {
        val definition = dashboardDefinitionRepository.loadDashboardDefinition()
        val html = if (definition == null) {
            createHTML().html { body { +"No saved definition found." } }
        } else {
            renderer.renderDashboard(definition)
        }
        return html
    }

    @PutMapping("/dashboard/definition", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveDashboardDefinition(@RequestBody dashboardDefinition: DashboardDefinition) {
        dashboardDefinitionRepository.saveDashboardDefinition(dashboardDefinition)
    }
}
