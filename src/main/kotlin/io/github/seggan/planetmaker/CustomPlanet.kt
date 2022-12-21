package io.github.seggan.planetmaker

import io.github.addoncommunity.galactifun.api.universe.PlanetaryObject
import io.github.addoncommunity.galactifun.api.universe.StarSystem
import io.github.addoncommunity.galactifun.api.universe.attributes.DayCycle
import io.github.addoncommunity.galactifun.api.universe.attributes.Gravity
import io.github.addoncommunity.galactifun.api.universe.attributes.Orbit
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Atmosphere
import io.github.addoncommunity.galactifun.api.universe.types.PlanetaryType
import io.github.addoncommunity.galactifun.api.worlds.AlienWorld
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import org.bukkit.inventory.ItemStack
import java.util.Random

class CustomPlanet : AlienWorld {

    constructor(
        name: String,
        type: PlanetaryType,
        orbit: Orbit,
        orbiting: PlanetaryObject,
        baseItem: ItemStack,
        dayCycle: DayCycle,
        atmosphere: Atmosphere,
        gravity: Gravity,
        generator: ChunkGenerator
    ) : super(name, type, orbit, orbiting, baseItem, dayCycle, atmosphere, gravity) {
        this.generator = generator
    }

    constructor(
        name: String,
        type: PlanetaryType,
        orbit: Orbit,
        orbiting: StarSystem,
        baseItem: ItemStack,
        dayCycle: DayCycle,
        atmosphere: Atmosphere,
        gravity: Gravity,
        generator: ChunkGenerator
    ) : super(name, type, orbit, orbiting, baseItem, dayCycle, atmosphere, gravity) {
        this.generator = generator
    }

    private val generator: ChunkGenerator

    override fun generateChunk(data: ChunkGenerator.ChunkData, random: Random, info: WorldInfo, x: Int, z: Int) {
    }

    override fun getPopulators(populators: MutableList<BlockPopulator>) {
    }

    override fun replaceChunkGenerator(defaultGenerator: ChunkGenerator): ChunkGenerator {
        return generator
    }
}