package io.github.seggan.planetmaker.conversion

import io.github.addoncommunity.galactifun.api.universe.PlanetaryObject
import io.github.addoncommunity.galactifun.api.universe.StarSystem
import io.github.addoncommunity.galactifun.api.universe.UniversalObject
import io.github.addoncommunity.galactifun.api.universe.attributes.DayCycle
import io.github.addoncommunity.galactifun.api.universe.attributes.Gravity
import io.github.addoncommunity.galactifun.api.universe.attributes.Orbit
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Atmosphere
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Gas
import io.github.addoncommunity.galactifun.api.universe.types.PlanetaryType
import io.github.addoncommunity.galactifun.api.worlds.AlienWorld
import io.github.addoncommunity.galactifun.base.BaseUniverse
import io.github.seggan.planetmaker.CustomPlanet
import io.github.seggan.planetmaker.fcl.FclObject
import io.github.seggan.planetmaker.fcl.collectProperties
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import java.math.BigDecimal

fun convertToPlanet(spec: FclObject, orbiting: UniversalObject = BaseUniverse.SOLAR_SYSTEM): AlienWorld {
    val name = spec.getString("name")

    val type = spec.getString("type", "TERRESTRIAL")
    val planetaryType = PlanetaryType.getById(type.idify())
        ?: PlanetaryType.allTypes().find { it.description().equals(type, ignoreCase = true) }
        ?: throw IllegalArgumentException("Unknown planet type $type")

    val orbit = Orbit.lightYears(
        spec.getNumber("orbitDistance").toDouble(),
        (spec.getNumber("year") / 24.0 / 365.25).toDouble()
    )
    val baseItem = ItemStack(Material.getMaterial(spec.getString("baseItem").idify()) ?: Material.STONE)
    val dayCycle = DayCycle.hours(spec.getNumber("dayLength").toInt())
    val atmosphere = spec.objects["atmosphere"]?.first()?.let(::getAtmosphere) ?: Atmosphere.NONE
    val gravity = Gravity.relativeToEarth(spec.getNumber("gravity").toDouble())

    val generator = getGenerator(spec)

    return when (orbiting) {
        is PlanetaryObject -> CustomPlanet(
            name,
            planetaryType,
            orbit,
            orbiting,
            baseItem,
            dayCycle,
            atmosphere,
            gravity,
            generator
        )
        is StarSystem -> CustomPlanet(
            name,
            planetaryType,
            orbit,
            orbiting,
            baseItem,
            dayCycle,
            atmosphere,
            gravity,
            generator
        )
        else -> throw IllegalArgumentException("Cannot orbit $orbiting")
    }
}

private fun getAtmosphere(spec: FclObject): Atmosphere {
    val pressure = spec.getNumber("pressure").toDouble()
    val storming = spec.getBoolean("storming", false)
    val thundering = spec.getBoolean("thundering", false)
    val weatherEnabled = spec.getBoolean("weatherEnabled", false)
    val environment = World.Environment.valueOf(spec.getString("environment", "NORMAL").idify())

    val gases = spec.objects["gases"]?.first().collectProperties { key, value ->
        Gas.valueOf(key.idify()) to (value as BigDecimal).toDouble()
    }.toMutableMap()

    val percent = gases.values.sum()
    if (percent > 101) {
        throw IllegalArgumentException("Gases add up to more than 100%")
    }
    gases[Gas.OTHER] = gases.getOrDefault(Gas.OTHER, 0.0) + (100 - percent)

    return Atmosphere(weatherEnabled, storming, thundering, environment, gases, pressure, mapOf())
}

private operator fun BigDecimal.div(other: Double): BigDecimal {
    return this.divide(BigDecimal.valueOf(other))
}

private fun String.idify(): String {
    return this.replace(' ', '_').uppercase()
}