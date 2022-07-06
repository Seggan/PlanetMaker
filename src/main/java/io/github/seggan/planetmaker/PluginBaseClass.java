package io.github.seggan.planetmaker;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class only exists because of some annoying differences regarding multiple inheritance in Kotlin and Java.
 */
public abstract class PluginBaseClass extends JavaPlugin implements SlimefunAddon {
}
