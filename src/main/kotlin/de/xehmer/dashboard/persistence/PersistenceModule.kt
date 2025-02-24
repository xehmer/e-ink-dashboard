package de.xehmer.dashboard.persistence

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "persistence", allowedDependencies = ["api"])
class PersistenceModule
