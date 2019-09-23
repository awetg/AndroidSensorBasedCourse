package com.example.sensorApp

import android.hardware.Sensor
import android.hardware.SensorEvent


class SensorItem (val imgResource: Int, val name: String, val sensorType: Int)

internal val sensorItemList: ArrayList<SensorItem> = arrayListOf(
    SensorItem(R.drawable.magnet, "Magnetic Sensor", Sensor.TYPE_MAGNETIC_FIELD),
    SensorItem(R.drawable.barometer, "Pressure Sensor", Sensor.TYPE_PRESSURE),
    SensorItem(R.drawable.bulb_on, "Light Sensor", Sensor.TYPE_LIGHT)
)


val magneticSensorHandler: (sensorEvent: SensorEvent?) -> Pair<Int, String>? = { sEvent ->
    sEvent?.values?.let {
        // to get absolute value sqrt of (x*x + y*y + z*z)
        val teslaXYZ = Math.sqrt(it[0].toDouble()*it[0] + it[1]*it[1] + it[2]*it[2])
        val txt = "X=${it[0].toInt()} Y=${it[1].toInt()} Z=${it[2].toInt()} Absolute = ${teslaXYZ.toInt()}uT"
        Pair(teslaXYZ.toInt(), txt)
    }
}

val pressureSensorHandler: (sensorEvent: SensorEvent?) -> Pair<Int, String>? = { sEvent ->
    sEvent?.values?.let {
        val txt = "Pressure = ${it[0]}mbar"
        Pair(it[0].toInt(),txt)
    }
}

val lightSensorHandler: (sensorEvent: SensorEvent?) -> Pair<Int, String>? = { sEvent ->
    sEvent?.values?.let {
        val txt = "Light = ${it[0]}lx"
        val img = if (it[0] > 20) R.drawable.bulb_on else R.drawable.bulb_off
        Pair(img,txt)
    }
}


internal val sensorDataHandler = mapOf(
    Sensor.TYPE_MAGNETIC_FIELD to magneticSensorHandler,
    Sensor.TYPE_PRESSURE to pressureSensorHandler,
    Sensor.TYPE_LIGHT to lightSensorHandler
)

internal val maxProgressBarValues = mapOf(
    Sensor.TYPE_MAGNETIC_FIELD to 200,
    Sensor.TYPE_PRESSURE to 1100
)

internal val sensorImageValues = mapOf(
    Sensor.TYPE_LIGHT to listOf(R.drawable.bulb_off, R.drawable.bulb_on)
)