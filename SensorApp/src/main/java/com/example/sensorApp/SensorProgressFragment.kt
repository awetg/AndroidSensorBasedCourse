package com.example.sensorApp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sensor_progress.*
import kotlinx.android.synthetic.main.fragment_sensor_progress.view.*


class SensorProgressFragment : Fragment(), SensorEventListener {

    private var sensorType: Int? = null
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sensorType = it.getInt(SENSOR_TYPE_KEY)
        }

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_sensor_progress, container, false)
        v.progressBar.max = maxProgressBarValues.get(sensorType)!!
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(sensorType: Int) =
            SensorProgressFragment().apply {
                arguments = Bundle().apply {
                    putInt(SENSOR_TYPE_KEY, sensorType)
                }
            }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.sensor == sensor) {
            sensorDataHandler.get(sensorType)?.invoke(sensorEvent)?.let {
                progressBar.setProgress(it.first)
                sensor_values_txt.text = it.second
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        sensor?.also {
            sensorManager.registerListener(
                this, it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
