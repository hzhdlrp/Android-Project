package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.User

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

//        val logInButton = findViewById<Button>(R.id.button2)
//        val signInButton = findViewById<Button>(R.id.button)
//        val dbHelper = MyDBHelper(this)
//
//        logInButton.setOnClickListener {
//            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
//            startActivity(intent)
//        }

        val signInButton  = findViewById<Button>(R.id.signInButton)
        val apikeyEditText = findViewById<EditText>(R.id.apikeyEditText)
        val secretKeyEditText = findViewById<EditText>(R.id.secretkeyEditText)

        signInButton.setOnClickListener {
            val user = User(apikeyEditText.text.toString(), secretKeyEditText.text.toString())
            val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }

//        fun generateSignature(secretKey: String, data: String): String {
//            val sha256HMAC = Mac.getInstance("HmacSHA256")
//            val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
//            sha256HMAC.init(secretKeySpec)
//            return sha256HMAC.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
//        }
//        val timestamp = System.currentTimeMillis()
//        val queryString = "timestamp=$timestamp"
//        val secretKey = "fqeSxVGw3JmcAspyifjnqpwUz2juFnzignBTA4XeCKLqeEGQ9dFGa7ouRUxIVRpB"
//        val signature = generateSignature(secretKey, queryString)
//        Log.d("aloha", "$signature  === $timestamp")

    }



}

