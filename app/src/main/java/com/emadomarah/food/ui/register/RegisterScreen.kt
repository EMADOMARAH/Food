package com.emadomarah.food.ui.register

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.emadomarah.food.R
import com.emadomarah.food.databinding.ActivityRegisterScreenBinding
import com.emadomarah.food.ui.home.HomeActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterScreen : AppCompatActivity() {

    // Initialize Firebase Auth
    private val uAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    var user: Map<String, Any> = HashMap()

    var email_txt: TextInputLayout? = null
    var password_txt:TextInputLayout? = null
    var current_weight_txt:TextInputLayout? = null
    var desired_weight_txt:TextInputLayout? = null
    var height_txt:TextInputLayout? = null
    var age_txt:TextInputLayout? = null
    var email: String? = null
    var password:String? = null
    var currentWieght:String? = null
    var desiredWeight:String? = null
    var height:String? = null
    var age:String? = null
    var uId: String? = null
    var bmr = 0.0
    var preferences: SharedPreferences? = null

    //gender
    var genderRadioGroup: RadioGroup? = null
    var gender_btn: RadioButton? = null
    var gender = "Male"
    var selectedId = 0

    lateinit var binding: ActivityRegisterScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_screen)


        window.statusBarColor = ContextCompat.getColor(this, R.color.registerstatebare)

        preferences = getSharedPreferences("Calories", MODE_PRIVATE)


        initRegisterViews()
    }
    private fun Register() {
        uAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uId = task.result.user!!.uid
                    StoreDataToFireStore()
                    preferences!!.edit().putString("uId", uId).apply()
                    preferences!!.edit().commit()
                    Toast.makeText(this, "Welcome....", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun StoreDataToFireStore() {
        MakeUserDataToMap()
        db.collection("users")
            .document(uId!!)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Registered Successfully")
                } else {
                    println("Registered Failed")
                }
            }
    }

    private fun MakeUserDataToMap() {

// Add elements to the map
        user = (user as HashMap).apply {
            put("email", email.toString())
            put("password", password.toString())
            put("currentWeight", currentWieght.toString())
            put("desiredWeight", desiredWeight.toString())
            put("height", height.toString())
            put("age", age.toString())
            put("gender", "male")
            put("bmr", bmr.toString())
            // Add other key-value pairs as needed
        }
    }

    private fun initRegisterViews() {
        email_txt = findViewById(R.id.register_input_email)
        password_txt = findViewById(R.id.register_input_password)
        current_weight_txt = findViewById(R.id.register_current_weight)
        desired_weight_txt = findViewById(R.id.register_desired_weight)
        height_txt = findViewById(R.id.register_height)
        age_txt = findViewById(R.id.register_age)
        genderRadioGroup = findViewById(R.id.gender_radio_group)
    }

    private fun GetDataFromRegisterViews() {
        email = email_txt!!.editText!!.text.toString().trim { it <= ' ' }
        password = password_txt!!.editText!!.text.toString().trim { it <= ' ' }
        currentWieght = current_weight_txt!!.editText!!.text.toString().trim { it <= ' ' }
        desiredWeight = desired_weight_txt!!.editText!!.text.toString().trim { it <= ' ' }
        height = height_txt!!.editText!!.text.toString().trim { it <= ' ' }
        age = age_txt!!.editText!!.text.toString().trim { it <= ' ' }
        selectedId = genderRadioGroup!!.checkedRadioButtonId
        gender_btn = findViewById(selectedId)
//        gender = if (gender_btn.getText().toString().matches("Male")) {
//            "Male"
//        } else {
//            "Female"
//        }
        val h = height!!.toLong()
        val k = currentWieght!!.toLong()
        when (gender) {
            "Male" -> {
                bmr = 10 * k + 6.25 * h - 5 * age!!.toLong() + 5
                bmr = bmr * 1.2
            }

            "Female" -> {
                bmr = 10 * k + 6.25 * h - 5 * age!!.toLong() - 161
                bmr = bmr * 1.2
            }
        }
        preferences!!.edit().putLong("bmr", bmr.toLong()).apply()
        preferences!!.edit().commit()
    }

    private fun ValidateData(): Boolean? {
        return if (email!!.isEmpty()) {
            email_txt!!.error = "Email is Required"
            email_txt!!.requestFocus()
            false
        } else if (password!!.isEmpty()) {
            password_txt!!.error = "Password is Required"
            password_txt!!.requestFocus()
            false
        } else if (currentWieght!!.isEmpty()) {
            current_weight_txt!!.error = "Current weight is Required"
            current_weight_txt!!.requestFocus()
            false
        } else if (desiredWeight!!.isEmpty()) {
            desired_weight_txt!!.error = "Desired weight is Required"
            desired_weight_txt!!.requestFocus()
            false
        } else if (height!!.isEmpty()) {
            height_txt!!.error = "Height is Required"
            height_txt!!.requestFocus()
            false
        } else if (age!!.isEmpty()) {
            age_txt!!.error = "Age is Required"
            age_txt!!.requestFocus()
            false
        } else {
            true
        }
    }


    fun RegisterOnClick(view: View) {
        when (view.id) {
            R.id.register_btn -> {
                GetDataFromRegisterViews()
                if (!ValidateData()!!) {
                    return
                }
                Register()
            }

            R.id.login_link -> onBackPressed()
        }
    }
}