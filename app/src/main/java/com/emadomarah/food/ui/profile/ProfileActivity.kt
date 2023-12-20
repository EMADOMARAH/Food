package com.emadomarah.food.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.emadomarah.food.R
import com.emadomarah.food.databinding.ActivityProfileBinding
import com.emadomarah.food.ui.login.LoginActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding

    var preferences: SharedPreferences? = null
    var doc: DocumentReference? = null
    var uId:kotlin.String? = null
    var bmr = 0.0
    var email_txt: String? = null
    var age_txt:String? = null
    var currentWieght_txt:String? = null
    var desiredWeight_txt:String? = null
    var height_txt:kotlin.String? = null
    var genderx: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        window.statusBarColor = ContextCompat.getColor(this, R.color.prifilestatebar)
        preferences = getSharedPreferences("Calories", MODE_PRIVATE)

        uId = preferences!!.getString("uId", "")

        doc = FirebaseFirestore.getInstance().document("users/$uId")
        getUserDataFromFirebase()


    }


    fun getUserDataFromFirebase() {
        doc!!.get()
            .addOnSuccessListener { documentSnapshot ->
                binding.profileInputEmail!!.setText(documentSnapshot.getString("email"))
                binding.profileCurrentWeight!!.setText(documentSnapshot.getString("currentWeight"))
                binding.profileDesiredWeight!!.setText(documentSnapshot.getString("desiredWeight"))
                binding.profileHeight!!.setText(documentSnapshot.getString("height"))
                binding.profileAge?.setText(documentSnapshot.getString("age"))
                genderx = documentSnapshot.getString("gender")
                binding.profileGender!!.text = "Gender : $genderx"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun extractDataFromScreen() {
        email_txt = binding.profileInputEmail.text.toString().trim { it <= ' ' }
        currentWieght_txt = binding.profileCurrentWeight.text.toString().trim { it <= ' ' }
        desiredWeight_txt = binding.profileDesiredWeight.getText().toString().trim { it <= ' ' }
        height_txt = binding.profileHeight.getText().toString().trim { it <= ' ' }
        age_txt = binding.profileAge.getText().toString().trim { it <= ' ' }
        val h: Long = height_txt!!.toLong()
        val k: Long = currentWieght_txt!!.toLong() / 100
        when (genderx) {
            "Male" -> bmr = 10 * k + 6.25 * h - 5 * age_txt!!.toLong() + 5
            "Female" -> bmr = 10 * k + 6.25 * h - 5 * age_txt!!.toLong() - 161
        }
        preferences!!.edit().putLong("bmr", bmr.toLong()).apply()
        preferences!!.edit().commit()
    }

    private fun validateUserData(): Boolean {
        return if (email_txt!!.isEmpty()) {
            binding.profileInputEmail.error = "Email is Required"
            binding.profileInputEmail.requestFocus()
            false
        } else if (currentWieght_txt!!.isEmpty()) {
            binding.profileCurrentWeight.error = "Current weight is Required"
            binding.profileCurrentWeight.requestFocus()
            false
        } else if (desiredWeight_txt!!.isEmpty()) {
            binding.profileDesiredWeight.setError("Desired weight is Required")
            binding.profileDesiredWeight.requestFocus()
            false
        } else if (height_txt!!.isEmpty()) {
            binding.profileHeight.setError("Height is Required")
            binding.profileHeight.requestFocus()
            false
        } else if (age_txt!!.isEmpty()) {
            binding.profileAge.setError("Age is Required")
            binding.profileAge.requestFocus()
            false
        } else {
            true
        }
    }


    fun updateUser() {
        doc!!.update(
            "currentWeight", currentWieght_txt,
            "desiredWeight", desiredWeight_txt,
            "height", height_txt,
            "age", age_txt
        )
    }



    fun ProfileOnClick(view: View) {
        when (view.id) {
            R.id.done_btn -> {
                extractDataFromScreen()
                if (validateUserData()) {
                    updateUser()
                    onBackPressed()
                }
            }

            R.id.logout_btn -> {
                preferences!!.edit().clear().apply()
                preferences!!.edit().commit()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}