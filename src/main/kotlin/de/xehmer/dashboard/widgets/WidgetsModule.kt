package de.xehmer.dashboard.widgets

import org.springframework.modulith.ApplicationModule

@ApplicationModule(id = "widgets", allowedDependencies = ["core :: *", "api", "utils"])
class WidgetsModule
