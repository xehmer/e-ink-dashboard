package de.xehmer.dashboard.core

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "core", allowedDependencies = ["api"])
class CoreModule
