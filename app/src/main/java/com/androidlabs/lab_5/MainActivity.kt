package com.androidlabs.lab_5

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.xr.runtime.math.toDegrees
import kotlin.math.atan2

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var levelView: LevelView

    private var orientationIndex = 0
    private val orientations = arrayOf(
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        if (savedInstanceState != null) {
            orientationIndex = savedInstanceState.getInt("orientationIndex", 0)
            requestedOrientation = orientations[orientationIndex]
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        }

        val rotateButton = findViewById<Button?>(R.id.rotateButton)
        if (rotateButton != null) {
            rotateButton.setOnClickListener { cycleOrientation() }
        } else {
            val rotateButtonTopLeft: Button = findViewById(R.id.rotateButtonTopLeft)
            val rotateButtonBottomRight: Button = findViewById(R.id.rotateButtonBottomRight)
            rotateButtonTopLeft.setOnClickListener { cycleOrientation() }
            rotateButtonBottomRight.setOnClickListener { cycleOrientation() }
        }

        levelView = findViewById(R.id.levelView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("orientationIndex", orientationIndex)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val rawX = it.values[0]
            val rawY = it.values[1]

            var angle = 0f
            when (orientations[orientationIndex]) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
                    angle = toDegrees(atan2(rawX.toDouble(), rawY.toDouble()).toFloat())
                }
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {
                    angle = toDegrees(atan2(rawY.toDouble(), -rawX.toDouble()).toFloat())
                    angle = normalizeAngle(angle - 180f)
                }
            }
            levelView.tiltAngle = angle
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    private fun cycleOrientation() {
        orientationIndex = (orientationIndex + 1) % orientations.size
        requestedOrientation = orientations[orientationIndex]
    }

    private fun normalizeAngle(angle: Float): Float {
        var normalized = ((angle % 360) + 360) % 360
        if (normalized > 180f) normalized -= 360f
        if (normalized == 180f || normalized == -180f) normalized = 0f
        return normalized
    }

}