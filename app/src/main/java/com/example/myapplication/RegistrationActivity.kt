package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.User
import com.example.myapplication.databinding.ActivityRegisterBinding

class RegistrationActivity : AppCompatActivity(), View.OnFocusChangeListener {

    private lateinit var registrationBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationBinding  = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(registrationBinding.root)
        registrationBinding.fullNameEditText.onFocusChangeListener = this
        registrationBinding.emailEditText.onFocusChangeListener = this
        registrationBinding.passwordEditText.onFocusChangeListener = this
        registrationBinding.confirmPasswordEditText.onFocusChangeListener = this

//        registrationBinding.registerButton.setOnClickListener {
//            if (validateFullName() && validateEmail() && validatePassword() && validateConfirmPassword()) {
//                val user = User(_id = "", fullName = registrationBinding.fullNameEditText.text.toString(),
//                    email = registrationBinding.emailEditText.text.toString())
//
//                val emailVerificationActivityIntent = Intent(this@RegistrationActivity, EmailVerificationActivity::class.java)
//                emailVerificationActivityIntent.putExtra("userEmail", user.getEmail())
//                startActivity(emailVerificationActivityIntent)
//            } else {
//                Toast.makeText(this, "Enter correct information", Toast.LENGTH_LONG).show()
//            }
//        }
    }


    public fun validateFullName() : Boolean {
        var errorMessage : String? = null
        val value : String = registrationBinding.fullNameEditText.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter your full name"
        }

        if (errorMessage != null) {
            registrationBinding.fullNameTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    public fun validateEmail() : Boolean {
        var errorMessage : String? = null
        val value : String = registrationBinding.emailEditText.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter an email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Invalid email"
        }

        if (errorMessage != null) {
            registrationBinding.emailTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    public fun validatePassword() : Boolean {
        var errorMessage : String? = null
        val value : String = registrationBinding.passwordEditText.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Enter a password"
        } else if (value.length < 6) {
            errorMessage = "Password must be at least 6 characters long"
        }

        if (errorMessage != null) {
            registrationBinding.passwordTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    public fun validateConfirmPassword() : Boolean {
        var errorMessage : String? = null
        val value : String = registrationBinding.passwordEditText.text.toString()
        val confirmValue : String = registrationBinding.confirmPasswordEditText.text.toString()
        if (value != confirmValue) {
            errorMessage = "Not the same password"
        }

        if (errorMessage != null) {
            registrationBinding.confirmPasswordTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v != null) {
            when (v.id) {
                R.id.fullNameEditText -> {
                    if (hasFocus) {
                        registrationBinding.fullNameTIL.isErrorEnabled = false
                    }
                    else  {
                        validateFullName()
                    }
                }

                R.id.emailEditText -> {
                    if (hasFocus) {
                    registrationBinding.emailTIL.isErrorEnabled = false
                    }
                    else  {
                        validateEmail()
                    }
                }

                R.id.passwordEditText -> {
                    if (hasFocus) {
                        registrationBinding.passwordTIL.isErrorEnabled = false
                    }
                    else  {
                        validatePassword()
                    }
                }

                R.id.confirmPasswordEditText -> {
                    if (hasFocus) {
                        registrationBinding.confirmPasswordTIL.isErrorEnabled = false
                    }
                    else  {
                        validateConfirmPassword()
                    }
                }
            }
        }
    }
}