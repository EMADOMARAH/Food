package com.emadomarah.food.ui.step_counter

import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emadomarah.food.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class StepCounterActivity : AppCompatActivity() {


    var stepsToken: TextView? = null
    var circularProgressBar: CircularProgressBar? = null
    var preferences: SharedPreferences? = null

    var sensorManager: SensorManager? = null
    var stepSensor: Sensor? = null

    private var magnitudePrevious = 0.0
    private var stepCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        preferences = getSharedPreferences("steps_pref", MODE_PRIVATE)

        stepsToken = findViewById(R.id.stepTaken)
        circularProgressBar = findViewById(R.id.circularProgressBar)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val stepDetector: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event != null) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val magnitude = Math.sqrt((x * x + y * y + z * z).toDouble())
                    val magnitudeDelta = magnitude - magnitudePrevious
                    magnitudePrevious = magnitude
                    if (magnitudeDelta > 6) {
                        stepCount++
                    }
                    ShowSteps(stepCount)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager!!.registerListener(
            stepDetector,
            stepSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )


        stepsToken?.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this,
                "Long tab to reset steps",
                Toast.LENGTH_SHORT
            ).show()
        })

        stepsToken?.setOnLongClickListener(OnLongClickListener {
            stepCount = 0
            preferences!!.edit().clear().apply()
            preferences!!.edit().commit()
            Toast.makeText(this, "Steps Reseted", Toast.LENGTH_SHORT).show()
            stepsToken!!.setText(stepCount.toString())
            ShowSteps(stepCount)
            true
        })


    }

    fun ShowSteps(step: Int) {
        stepsToken!!.text = step.toString()
        circularProgressBar!!.progress = step.toFloat()
    }


    override fun onResume() {
        super.onResume()
        stepCount = preferences!!.getInt("steps", 0)
    }

    override fun onPause() {
        super.onPause()
        preferences!!.edit().putInt("steps", stepCount).apply()
        preferences!!.edit().commit()
    }

    override fun onStop() {
        super.onStop()
        preferences!!.edit().putInt("steps", stepCount).apply()
        preferences!!.edit().commit()
    }
}
