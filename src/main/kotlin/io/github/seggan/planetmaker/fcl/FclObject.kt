package io.github.seggan.planetmaker.fcl

class FclObject(val name: String, val properties: Map<String, Any>, objects: List<FclObject>) {

    val objects = objects.associateBy { it.name }

    override fun toString(): String {
        return "$name { ${properties.map { "${it.key}: ${it.value}" }.joinToString(", ")}${if (objects.isNotEmpty()) ", " else ""}${objects.map { it.value }.joinToString(", ")} }"
    }
}