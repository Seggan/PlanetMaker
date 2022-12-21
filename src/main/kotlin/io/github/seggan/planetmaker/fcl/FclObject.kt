package io.github.seggan.planetmaker.fcl

import java.math.BigDecimal

class FclObject(val name: String, val properties: Map<String, Any>, objects: List<FclObject>) {

    val objects = objects.groupBy { it.name }

    inline fun <reified T> get(key: String, default: T? = null): T {
        val value = properties[key] ?: default ?: throw NoSuchElementException(
            "No $key property in $name"
        )
        return value as? T ?: throw IllegalArgumentException("$key in $name is not of type ${T::class.simpleName}")
    }

    fun getString(key: String, default: String? = null): String = get(key, default)

    fun getNumber(key: String, default: BigDecimal? = null): BigDecimal = get(key, default)

    fun getBoolean(key: String, default: Boolean? = null): Boolean = get(key, default)

    override fun toString(): String {
        return "$name { ${properties.map { "${it.key}: ${it.value}" }.joinToString(", ")}${if (objects.isNotEmpty()) ", " else ""}${objects.map { it.value }.joinToString(", ")} }"
    }
}

fun <K, V> FclObject?.collectProperties(transform: (String, Any) -> Pair<K, V>): Map<K, V> {
    if (this == null) return mapOf()
    return properties.entries.associate { transform(it.key, it.value) }
}