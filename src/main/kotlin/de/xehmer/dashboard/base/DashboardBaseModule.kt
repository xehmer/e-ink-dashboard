package de.xehmer.dashboard.base

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "base", allowedDependencies = ["api"])
class DashboardBaseModule
