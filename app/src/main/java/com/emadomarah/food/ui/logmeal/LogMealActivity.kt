package com.emadomarah.food.ui.logmeal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emadomarah.food.R
import com.emadomarah.food.adapter.LogmealAdapter
import com.emadomarah.food.model.LogmealItemModel
import com.emadomarah.food.ui.pic.PicActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat

class LogMealActivity : AppCompatActivity() {

    var myDate: String? = null
    var mealTitle:String? = null
    var mealTilte: TextView? = null
    var mealTotalCalories:TextView? = null
    var mealTotalCom:TextView? = null
    var pref: SharedPreferences? = null
    var resultName: String? = null
    var resultCal:kotlin.String? = null
    var resultFat:kotlin.String? = null
    var resultCarb:kotlin.String? = null
    var resultProtein:kotlin.String? = null

    var foodRecycler: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var logmealItemModel: LogmealItemModel? = null
    var myFoodModelList: ArrayList<LogmealItemModel>? = null


    var db = FirebaseFirestore.getInstance()

    //List of document Names
    var foodElement: List<String>? = null

    //List of Document Content
    var foodMapList: MutableList<Map<String, Any>>? = null

    //toatal
    var totalCalories = 0.0
    var totalFat: //toatal
    kotlin.Double = 0.0
    var totalProtein = 0.0
    var totalCarb: //toatal
    kotlin.Double = 0.0
    var logmealAdapter: LogmealAdapter? = null

    var counter = 0

    var uId: String? = null
    var nf = NumberFormat.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_meal)
        nf.maximumFractionDigits = 0

        window.statusBarColor = ContextCompat.getColor(this, R.color.logmealstatebar)

        pref = getSharedPreferences("Calories", MODE_PRIVATE)
        uId = pref!!.getString("uId", "")

        initViews()
        myFoodModelList = ArrayList()


        //list contain food documents names
        foodElement = ArrayList()
        foodMapList = ArrayList()

        //get data from home screen (date , meal name)
        val extras = intent.extras
        if (extras != null) {
            myDate = extras.getString("date")
            mealTitle = extras.getString("title")
            mealTilte!!.text = mealTitle
        }

        getAllFoodElements()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        myFoodModelList!!.clear()
        foodMapList!!.clear()
        totalCalories = 0.0
        totalFat = 0.0
        totalProtein = 0.0
        totalCarb = 0.0
        getAllFoodElements()
    }

    fun getAllFoodElements() {
        db.collection("food/$uId/dates/$myDate/$mealTitle").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        //System.out.println(document.getId() + " => " + document.getData());
                        foodMapList!!.add(document.data)
                    }
                    for (i in foodMapList!!.indices) {
                        totalCalories = totalCalories + foodMapList!![i]["calories"].toString().toDouble()
                        totalFat = totalFat + foodMapList!![i]["fat"].toString().toDouble()
                        totalProtein = totalFat + foodMapList!![i]["protien"].toString().toDouble()
                        totalCarb = totalFat + foodMapList!![i]["carb"].toString().toDouble()
                        myFoodModelList!!.add(
                            LogmealItemModel(
                                foodMapList!![i]["foodName"].toString(),
                                foodMapList!![i]["calories"].toString().toDouble(),
                                foodMapList!![i]["amount"].toString().toDouble()
                            )
                        )
                    }

                    // myFoodModelList.add(new LogmealItemModel("Apple" , 90.0, 100.0));
                    mealTotalCalories!!.text =
                        "Total Calories : " + nf.format(totalCalories) + " Cal"
                    mealTotalCom!!.text =
                        "Fat : " + nf.format(totalFat) + "g, Protein : " + nf.format(totalProtein) + "g, Carb : " + nf.format(
                            totalCarb
                        ) + "g"
                    logmealAdapter = LogmealAdapter(myFoodModelList)
                    foodRecycler!!.adapter = logmealAdapter
                } else {
                    Toast.makeText(this, "Path Error", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveToDatabase() {
        counter = 0
        while (counter < foodMapList!!.size) {
            db.collection("food/$uId/dates/$myDate/$mealTitle")
                .document(foodMapList!![counter]["foodName"].toString())
                .delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Fail here", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "fail with delete",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            counter++
        }
        counter = 0
        while (counter < foodMapList!!.size) {
            db.collection("food/$uId/dates/$myDate/$mealTitle")
                .document(foodMapList!![counter]["foodName"].toString())
                .set(foodMapList!![counter])
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Error : $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            counter++
        }
    }


    fun initViews() {
        mealTilte = findViewById(R.id.logmeal_meal_title)
        mealTotalCalories = findViewById(R.id.logmeal_total_cal)
        mealTotalCom = findViewById(R.id.logmeal_total_comp)
        foodRecycler = findViewById(R.id.logmeal_Recycler)
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        foodRecycler!!.setLayoutManager(layoutManager)
    }

    fun LogmealOnClick(view: View) {
        when (view.id) {
            R.id.logmeal_add_food -> {
                val i = Intent(this, PicActivity::class.java)
                i.putExtra("date", myDate)
                i.putExtra("title", mealTitle)
                startActivityForResult(i, 1)
            }

            R.id.logmeal_add_btn -> if (foodMapList!!.size > 0) {
                saveToDatabase()
            } else {
                Toast.makeText(this, "No Meal To Save", Toast.LENGTH_SHORT).show()
            }

            R.id.rec_back_btn -> onBackPressed()
            R.id.logmeal_delete_food -> AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to delete all food?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, whichButton ->
                    counter = 0
                    while (counter < foodMapList!!.size) {
                        db.collection("food/$uId/dates/$myDate/$mealTitle").document(
                            foodMapList!![counter]["foodName"].toString()
                        )
                            .delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "All Meals Deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onBackPressed()
                                } else {
                                    Toast.makeText(this, "Fail here", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "fail with delete",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        counter++
                    }
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
    }
}