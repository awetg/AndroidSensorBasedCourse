package com.example.sensorApp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

const val SENSOR_TYPE_KEY = "SENSOR_KEY"

class MainActivity : AppCompatActivity() {
//
    private lateinit var sensorManager: SensorManager
    private val progressSensors = listOf(
        Sensor.TYPE_MAGNETIC_FIELD,
        Sensor.TYPE_PRESSURE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SensorRecyclerAdapter()
        adapter.setClickListener {
            if (sensorManager.getSensorList(it.sensorType).size > 0 ) {
                if(progressSensors.contains(it.sensorType)) {
                    addFragment(SensorProgressFragment.newInstance(it.sensorType))
                } else {
                    addFragment(SensorImageFragment.newInstance(it.sensorType))
                }
            } else {
                Toast.makeText(this, "Sorry, your device does not support the sensor", Toast.LENGTH_LONG).show()
            }
        }
        sensor_recyclerView.adapter = adapter
        sensor_recyclerView.layoutManager = LinearLayoutManager(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }


    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frag_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
