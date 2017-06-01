package io.data2viz.math

import kotlin.js.Math

val EPS = 1e-6
val EPS2 = EPS * EPS
val PI = Math.PI
val halfPI = PI / 2
val THETA = PI * 2
val THETA_EPS = THETA - EPS
val DEG_TO_RAD = PI / 180
val RAD_TO_DEG = 180 / PI

val Number.deg:Angle
    get() = Angle(toDouble() * DEG_TO_RAD)

data class Angle(val rad: Double){
    val cos:Double get() = Math.cos(rad)
    val sin:Double get() = Math.sin(rad)
    val tan:Double get() = Math.tan(rad)
    val deg:Double get() = rad * RAD_TO_DEG

    operator fun plus(angle: Angle)     = Angle(rad + angle.rad)
    operator fun minus(angle: Angle)    = Angle(rad - angle.rad)
    operator fun times(d: Number)       = Angle(rad * d.toDouble())
    operator fun div(d: Number)         = Angle(rad / d.toDouble())
}
