package com.emadomarah.food.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.emadomarah.food.R
import com.emadomarah.food.ui.home.HomeActivity
import com.emadomarah.food.ui.register.RegisterScreen
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private val uAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    var email_txt: TextInputLayout? = null
    var password_txt:TextInputLayout? = null
    var email: String? = null
    var password:kotlin.String? = null
    var uId: String? = null

    var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.statusBarColor = ContextCompat.getColor(this, R.color.loginstatebar)

        preferences = getSharedPreferences("Calories", MODE_PRIVATE)

        if (preferences!!.contains("uId")) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        } else {
            //Toast.makeText(this, "NOOOOOOOOOOO", Toast.LENGTH_SHORT).show();
        }
        initLoginViews()


    }

    private fun initLoginViews() {
        email_txt = findViewById(R.id.signin_input_email)
        password_txt = findViewById(R.id.signin_input_password)
    }

    fun LoginClicked(view: View) {
        when (view.id) {
            R.id.register_link -> startActivity(Intent(applicationContext, RegisterScreen::class.java))
            R.id.loginbtn -> {
                getDataFromScreen()
                if (!validateUserData()!!) {
                    return
                }
                userLogin()
            }
        }
    }


    private fun getBmrFromFirebase() {
        db.collection("users").document(uId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val bmi = documentSnapshot.getString("bmr")
                val bm = bmi!!.toDouble()
                preferences!!.edit().putLong("bmr", bm.toLong()).apply()
                preferences!!.edit().commit()
            }
            .addOnFailureListener { Toast.makeText(this, "Failed Listener in get Data (BMR)", Toast.LENGTH_SHORT).show() }
    }

    private fun userLogin() {
        uAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uId = task.result.user!!.uid
                    preferences!!.edit().putString("uId", uId).apply()
                    preferences!!.edit().commit()
                    getBmrFromFirebase()
                    Toast.makeText(this, "Welcome...", Toast.LENGTH_SHORT).show()
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
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed Listener in Login", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateUserData(): Boolean? {
        return if (email!!.isEmpty()) {
            email_txt!!.error = "Email is Required"
            email_txt!!.requestFocus()
            false
        } else if (password!!.isEmpty()) {
            password_txt!!.error = "Password is Required"
            password_txt!!.requestFocus()
            false
        } else {
            true
        }
    }

    private fun getDataFromScreen() {
        email = email_txt!!.editText!!.text.toString().trim { it <= ' ' }
        password = password_txt!!.editText!!.text.toString().trim { it <= ' ' }
    }
}