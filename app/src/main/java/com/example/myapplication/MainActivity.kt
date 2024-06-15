package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.MyDBHelper
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
    }



}

