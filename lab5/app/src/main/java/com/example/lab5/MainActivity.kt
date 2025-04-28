package com.example.lab5

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var compassImage: ImageView
    private lateinit var arrowImage: ImageView
    private lateinit var degreeText: TextView
    private var currentDegree = 0f
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compassImage = findViewById(R.id.compassImage)
        arrowImage = findViewById(R.id.arrowImage)
        degreeText = findViewById(R.id.degreeText)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
        }

        if (gravity != null && geomagnetic != null) {
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360) % 360
                rotateCompass(azimuth)
                updateDirectionText(azimuth)
            }
        }
    }

    private fun rotateCompass(azimuth: Float) {
        val rotateAnimation = RotateAnimation(
            currentDegree,
            -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnimation.duration = 500
        rotateAnimation.fillAfter = true
        arrowImage.startAnimation(rotateAnimation)
        currentDegree = -azimuth
    }

    private fun updateDirectionText(azimuth: Float) {
        val direction = when {
            azimuth >= 350 || azimuth <= 10 -> "N"
            azimuth in 80.0..100.0 -> "E"
            azimuth in 170.0..190.0 -> "S"
            azimuth in 260.0..280.0 -> "W"
            else -> ""
        }
        degreeText.text = String.format("%.0f° %s", azimuth, direction)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
