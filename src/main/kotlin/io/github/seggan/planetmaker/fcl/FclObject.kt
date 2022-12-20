package io.github.seggan.planetmaker.fcl

data class FclObject(val name: String, val properties: Map<String, Any>, val objects: List<FclObject>) {

    fun getProperty(name: String): Any? {
        return properties[name]
    }

    fun getObject(name: String): FclObject? {
        return objects.firstOrNull { it.name == name }
    }

    override fun toString(): String {
        return "$name { ${properties.map { "${it.key}: ${it.value}" }.joinToString(", ")}${if (objects.isNotEmpty()) ", " else ""}${objects.joinToString(", ")} }"
    }
}