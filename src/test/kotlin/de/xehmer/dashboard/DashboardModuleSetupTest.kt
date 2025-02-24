package de.xehmer.dashboard

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class DashboardModuleSetupTest {
    @Test
    fun verifyModules() {
        val modules = ApplicationModules.of(DashboardApplication::class.java)
        modules.verify()
        Documenter(modules).writeDocumentation()
    }
}
