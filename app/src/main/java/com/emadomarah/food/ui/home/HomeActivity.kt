package com.emadomarah.food.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.emadomarah.food.MainActivity
import com.emadomarah.food.R
import com.emadomarah.food.ui.logmeal.LogMealActivity
import com.emadomarah.food.ui.profile.ProfileActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date

class HomeActivity : AppCompatActivity() {

    var date: Button? = null
    var calender: Date? = null
    var formattedDate: String? = null
    var year = 0
    var month:Int = 0
    var day:Int = 0
    var myDate: String? = null

    //StepCounter------------------------------------------------
    var stepsToken: TextView? = null
    var burned: TextView? = null
    var target:TextView? = null
    var lTarget: Long = 0
    var sensorManager: SensorManager? = null
    var stepSensor: Sensor? = null
    var preferences: SharedPreferences? = null
    var jpref:SharedPreferences? = null
    var uId: String? = null

    private var magnitudePrevious = 0.0
    private var stepCount = 0

    //-----------------------------------------------------------
    var totalCalories = 0.0
    var totalFat:Double = 0.0
    var totalProtein= 0.0
    var totalCarb:Double = 0.0
    var db = FirebaseFirestore.getInstance()
    lateinit var breakfastMapList: MutableList<Map<String, Any>>
    lateinit var  lunchMapList: MutableList<Map<String, Any>>
    lateinit var  dinnerMapList: MutableList<Map<String, Any>>
    var bfCalories = 0.0
    var bfFat:kotlin.Double = 0.0
    var bfProtein:kotlin.Double = 0.0
    var bfCarb:kotlin.Double = 0.0
    var lunchCalories = 0.0
    var lunchFat:kotlin.Double = 0.0
    var lunchProtein:kotlin.Double = 0.0
    var lunchCarb:kotlin.Double = 0.0
    var dinnerCalories = 0.0
    var dinnerFat:kotlin.Double = 0.0
    var dinnerProtein:kotlin.Double = 0.0
    var dinnerCarb:kotlin.Double = 0.0

    var allCalories: TextView? = null
    var allFats:TextView? = null
    var allProtein:TextView? = null
    var allCarb:TextView? = null
    var caloriesLeft:TextView? = null
    var bCalories: TextView? = null
    var brackfastInfo:TextView? = null
    var lCalories: TextView? = null
    var lunchInfo:TextView? = null
    var dCalories: TextView? = null
    var dinnerInfo:TextView? = null
    var nf = NumberFormat.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nf.maximumFractionDigits = 0
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.homestatebar
        ) //status bar or the time bar at the top


        breakfastMapList = ArrayList()
        lunchMapList = ArrayList()
        dinnerMapList = ArrayList()
        preferences = getSharedPreferences("steps_pref", MODE_PRIVATE)
        jpref = getSharedPreferences("Calories", MODE_PRIVATE)
        uId = jpref!!.getString("uId", "")


        initalViews()


        val calender = Calendar.getInstance()
        year = calender[Calendar.YEAR]
        month = calender[Calendar.MONTH]
        day = calender[Calendar.DAY_OF_MONTH]
        myDate = day.toString() + "-" + (month + 1) + "-" + year
        //myDate = "29-5-2021";
        //myDate = "29-5-2021";
        date!!.text = myDate


//-----------------------------------sensor----------------------------------------------------------------


//-----------------------------------sensor----------------------------------------------------------------
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

        stepsToken!!.setOnClickListener {
            Toast.makeText(
                this,
                "Long tab to reset steps",
                Toast.LENGTH_SHORT
            ).show()
        }

        stepsToken!!.setOnLongClickListener {
            stepCount = 0
            preferences!!.edit().clear().apply()
            preferences!!.edit().commit()
            Toast.makeText(this, "Steps Reseted", Toast.LENGTH_SHORT).show()
            stepsToken!!.text = stepCount.toString()
            ShowSteps(stepCount)
            true
        }
//----------------------------------------------------------------------------------------------------------

        //----------------------------------------------------------------------------------------------------------
        getBreakfastFoodElements()
        getLunchElements()
        getDinnerElements()
    }

    fun getBreakfastFoodElements() {
        db.collection("food/$uId/dates/$myDate/Breakfast").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //System.out.println(document.getId() + " => " + document.getData());
                       if (breakfastMapList != null){
                           breakfastMapList.add(document.data)
                       }

                    }
                    for (i in breakfastMapList!!.indices) {
                        bfCalories =
                            bfCalories + breakfastMapList!![i]["calories"].toString().toDouble()
                        bfFat = bfFat + breakfastMapList!![i]["fat"].toString().toDouble()
                        bfProtein =
                            bfProtein + breakfastMapList!![i]["protien"].toString().toDouble()
                        bfCarb = bfCarb + breakfastMapList!![i]["carb"].toString().toDouble()
                    }
                    if (bfCalories > 0) {
                        totalCalories = totalCalories + bfCalories
                        totalCarb = totalCarb + bfCarb
                        totalProtein = totalProtein + bfProtein
                        totalFat = totalFat + bfFat
                    }
                    if (!breakfastMapList!!.isEmpty()) {
                        bCalories!!.text = nf.format(bfCalories).toString()
                        brackfastInfo!!.text =
                            nf.format(bfFat).toString() + "g Fat, " + nf.format(bfProtein)
                                .toString() + "g Protein, " + nf.format(bfCarb)
                                .toString() + "g Carb"
                    } else {
                    }
                    setTotalInfo()
                } else {
                    Toast.makeText(applicationContext, "breakfast not success", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).show()
            }
    }

    fun getLunchElements() {
        db.collection("food/$uId/dates/$myDate/Lunch").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //System.out.println(document.getId() + " => " + document.getData());
                        lunchMapList.add(document.data)
                    }
                    for (i in lunchMapList!!.indices) {
                        lunchCalories =
                            lunchCalories + lunchMapList!![i]["calories"].toString().toDouble()
                        lunchFat = lunchFat + lunchMapList!![i]["fat"].toString().toDouble()
                        lunchProtein =
                            lunchProtein + lunchMapList!![i]["protien"].toString().toDouble()
                        lunchCarb = lunchCarb + lunchMapList!![i]["carb"].toString().toDouble()
                    }
                    if (!lunchMapList!!.isEmpty()) {
                        lCalories!!.text = nf.format(lunchCalories).toString()
                        lunchInfo!!.text =
                            nf.format(lunchFat).toString() + "g Fat, " + nf.format(lunchProtein)
                                .toString() + "g Protein, " + nf.format(lunchCarb)
                                .toString() + "g Carb"
                    }
                    totalCalories = totalCalories + lunchCalories
                    totalCarb = totalCarb + lunchCarb
                    totalProtein = totalProtein + lunchProtein
                    totalFat = totalFat + lunchFat
                    setTotalInfo()
                } else {
                    Toast.makeText(applicationContext, "Lunch not success", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Failed in Lunch",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getDinnerElements() {
        db.collection("food/$uId/dates/$myDate/Dinner").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //System.out.println(document.getId() + " => " + document.getData());
                        dinnerMapList.add(document.data)
                    }
                    for (i in dinnerMapList!!.indices) {
                        dinnerCalories = dinnerCalories + Math.round(
                            dinnerMapList!![i]["calories"].toString().toDouble()
                        )
                        dinnerFat = dinnerFat + dinnerMapList!![i]["fat"].toString().toDouble()
                        dinnerProtein =
                            dinnerProtein + dinnerMapList!![i]["protien"].toString().toDouble()
                        dinnerCarb = dinnerCarb + dinnerMapList!![i]["carb"].toString().toDouble()
                    }
                    totalCalories = totalCalories + dinnerCalories
                    totalCarb = totalCarb + dinnerCarb
                    totalProtein = totalProtein + dinnerProtein
                    totalFat = totalFat + dinnerFat
                    if (!dinnerMapList!!.isEmpty()) {
                        dCalories!!.text = nf.format(dinnerCalories).toString()
                        dinnerInfo!!.text =
                            nf.format(dinnerFat).toString() + "g Fat, " + nf.format(dinnerProtein)
                                .toString() + "g Protein, " + nf.format(dinnerCarb)
                                .toString() + "g Carb"
                    }
                    setTotalInfo()
                } else {
                    Toast.makeText(applicationContext, "Dinner not success", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Failed in Dinner",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun setTotalInfo() {
        allCalories!!.text = nf.format(totalCalories).toString()
        allProtein!!.text = nf.format(totalProtein).toString()
        allFats!!.text = nf.format(totalFat).toString()
        allCarb!!.text = nf.format(totalCarb).toString()
        caloriesLeft!!.text = (lTarget - totalCalories.toLong()).toString()
    }


    fun ShowSteps(step: Int) {
        stepsToken!!.text = step.toString()
        burned!!.text = Math.round(step * 1.2).toString()
        target!!.text = lTarget.toInt().toString()
    }


    override fun onRestart() {
        super.onRestart()
        recreate()
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

    private fun initalViews() {
        //steps
        db.collection("users").document(uId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val target = documentSnapshot.getString("bmr")
                lTarget = target!!.toDouble().toLong()
            }
        lTarget = jpref!!.getLong("bmr", 0.0.toLong())
        target = findViewById(R.id.target)
        target!!.setText(lTarget.toInt().toString())
        stepsToken = findViewById(R.id.steps)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //-------------------------------------
        date = findViewById(R.id.date_btn)
        burned = findViewById(R.id.burned)
        //---------------------------------------------
        allCalories = findViewById(R.id.total_calories)
        allCarb = findViewById(R.id.total_carb)
        allFats = findViewById(R.id.total_fat)
        allProtein = findViewById(R.id.total_protein)
        caloriesLeft = findViewById(R.id.calories_left)
        bCalories = findViewById(R.id.brackfast_calories)
        brackfastInfo = findViewById(R.id.breakfast_info)
        lCalories = findViewById(R.id.lunch_calories)
        lunchInfo = findViewById(R.id.lunch_info)
        dCalories = findViewById(R.id.dinner_calories)
        dinnerInfo = findViewById(R.id.dinner_info)
    }

    fun resetData() {
        lunchMapList.clear()
        breakfastMapList.clear()
        dinnerMapList.clear()
        totalCalories = 0.0
        totalCarb = 0.0
        totalFat = 0.0
        totalProtein = 0.0
        bfCalories = 0.0
        bfFat = 0.0
        bfProtein = 0.0
        bfCarb = 0.0
        lunchCalories = 0.0
        lunchFat = 0.0
        lunchProtein = 0.0
        lunchCarb = 0.0
        dinnerCalories = 0.0
        dinnerFat = 0.0
        dinnerProtein = 0.0
        dinnerCarb = 0.0
        allCarb!!.text = "0"
        allFats!!.text = "0"
        allProtein!!.text = "0"
        allCalories!!.text = "0"
        bCalories!!.text = "0"
        brackfastInfo!!.text = ""
        lCalories!!.text = "0"
        lunchInfo!!.text = ""
        dCalories!!.text = "0"
        dinnerInfo!!.text = ""
    }


    fun HomeOnClick(view: View) {
        when (view.id) {
            R.id.recommend -> startActivity(Intent(applicationContext, MainActivity::class.java))
            R.id.profile ->                 //startActivity(new Intent(getApplicationContext(), Pic.class));
                startActivity(Intent(applicationContext, ProfileActivity::class.java))

            R.id.date_btn -> {
                val datePickerDialog = DatePickerDialog(this,
                    { datePicker, year, month, day ->
                        myDate = day.toString() + "-" + (month + 1) + "-" + year
                        date!!.text = myDate
                        resetData()
                        getBreakfastFoodElements()
                        getLunchElements()
                        getDinnerElements()
                    }, year, month, day
                )
                datePickerDialog.show()
            }

            R.id.home_breakfast -> {
                val intent1 = Intent(applicationContext, LogMealActivity::class.java)
                intent1.putExtra("title", "Breakfast")
                intent1.putExtra("date", myDate)
                startActivityForResult(intent1, 1)
            }

            R.id.home_lunch -> {
                val intent2 = Intent(applicationContext, LogMealActivity::class.java)
                intent2.putExtra("title", "Lunch")
                intent2.putExtra("date", myDate)
                startActivityForResult(intent2, 1)
            }

            R.id.home_dinner -> {
                val intent3 = Intent(applicationContext, LogMealActivity::class.java)
                intent3.putExtra("title", "Dinner")
                intent3.putExtra("date", myDate)
                startActivityForResult(intent3, 1)
            }
        }
    }
}