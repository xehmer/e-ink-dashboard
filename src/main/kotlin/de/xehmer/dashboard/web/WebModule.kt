package de.xehmer.dashboard.web

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "web", allowedDependencies = ["core :: *", "api", "persistence"])
class WebModule
