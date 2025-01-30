import java.net.URI

rootProject.name = "dashboard"

sourceControl {
    gitRepository(URI.create("https://github.com/schildbach/public-transport-enabler.git")) {
        producesModule("de.schildbach.pte:public-transport-enabler")
    }
}
