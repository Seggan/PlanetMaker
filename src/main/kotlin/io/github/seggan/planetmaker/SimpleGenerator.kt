package io.github.seggan.planetmaker

import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.Random

abstract class SimpleGenerator : ChunkGenerator() {

    abstract fun generateChunk(data: ChunkData, random: Random, info: WorldInfo, x: Int, z: Int)
}