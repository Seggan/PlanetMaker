package io.github.seggan.planetmaker

import org.bukkit.plugin.java.JavaPlugin

class PlanetMaker : PluginBaseClass() {

    companion object {
        lateinit var instance: PlanetMaker
            private set
    }

    override fun onEnable() {
        instance = this
    }

    override fun getJavaPlugin(): JavaPlugin = this

    override fun getBugTrackerURL(): String? = null

}