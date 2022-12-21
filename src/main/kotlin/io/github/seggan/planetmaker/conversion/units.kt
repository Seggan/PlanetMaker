package io.github.seggan.planetmaker.conversion

import java.math.BigDecimal

fun resolveUnit(name: String, value: Any, unit: String): Any {
    if (value !is BigDecimal) return value
    val actualUnit = aliases[unit] ?: unit
    val ratio = unitConversions[actualUnit] ?: throw IllegalArgumentException("Unknown unit $unit")
    return value * ratio
}

private val aliases = mapOf(
    "pc" to "parsecs",
    "parsec" to "parsecs",
    "ly" to "light years",
    "light year" to "light years",
    "lightyears" to "light years",
    "lightyear" to "light years",
    "au" to "astronomical units",
    "astronomical unit" to "astronomical units",
    "astronomicalunits" to "astronomical units",
    "astronomicalunit" to "astronomical units",
    "km" to "kilometers",
    "kilometer" to "kilometers",

    "m/s^2" to "meters per second squared",
    "m/s2" to "meters per second squared",
    "ms^2" to "meters per second squared",
    "ms2" to "meters per second squared",
    "meter per second squared" to "meters per second squared",
    "earth gravity" to "g",
    "earth gravities" to "g",
    "gs" to "g",

    "s" to "seconds",
    "sec" to "seconds",
    "secs" to "seconds",
    "second" to "seconds",
    "m" to "minutes",
    "min" to "minutes",
    "mins" to "minutes",
    "minute" to "minutes",
    "h" to "hours",
    "hr" to "hours",
    "hrs" to "hours",
    "hour" to "hours",
    "d" to "days",
    "day" to "days",
    "y" to "years",
    "yr" to "years",
    "yrs" to "years",
    "year" to "years",

    "pa" to "pascals",
    "pascal" to "pascals",
    "bar" to "bars",
    "atm" to "atmospheres",
)

private val unitConversions = mapOf(
    // Distance to light years
    "parsecs" to "3.26156",
    "light years" to "1",
    "astronomical units" to "0.00001581",
    "kilometers" to "0.0000000000001057",

    // Acceleration to earth gravities
    "meters per second squared" to "0.102",
    "g" to "1",

    // Time to hours
    "seconds" to "0.000277778",
    "minutes" to "0.0166667",
    "hours" to "1",
    "days" to "24",
    "years" to "8760",

    // Pressure to atmospheres
    "pascals" to "0.000009869",
    "bars" to "0.986923",
    "atmospheres" to "1",
).mapValues { (_, v) -> v.toBigDecimal() }