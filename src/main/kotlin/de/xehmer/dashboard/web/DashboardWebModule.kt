package de.xehmer.dashboard.web

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "web", allowedDependencies = ["base", "api", "persistence"])
class DashboardWebModule
