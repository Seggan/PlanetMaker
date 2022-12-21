package io.github.seggan.planetmaker

import io.github.seggan.planetmaker.fcl.parsing.FclLexer
import io.github.seggan.planetmaker.fcl.parsing.FclParser
import io.github.seggan.planetmaker.fcl.resolveUnit
import org.bukkit.plugin.java.JavaPlugin

class PlanetMaker : PluginBaseClass() {

    companion object {
        lateinit var instance: PlanetMaker
            private set
    }

    override fun onEnable() {
        instance = this

        logger.info("PlanetMaker has been enabled")
        logger.info("Loading planets...")

        val planetFolder = dataFolder.resolve("planets")
        planetFolder.mkdirs()

        planetFolder.listFiles()?.forEach { file ->
            if (file.extension == "fcl") {
                logger.info("Loading planet ${file.nameWithoutExtension}")
                val lexer = FclLexer(file.readText())
                val parser = FclParser(lexer.lex(), ::resolveUnit)
                val planet = parser.parse()
            }
        }
    }

    override fun getJavaPlugin(): JavaPlugin = this

    override fun getBugTrackerURL(): String? = null

}