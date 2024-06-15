package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.EmailVerification
import com.example.myapplication.databinding.ActivityEmailVerificationBinding


class EmailVerificationActivity : AppCompatActivity() {
    private lateinit var emailVerificationBinding: ActivityEmailVerificationBinding
    private lateinit var countDownTimer: CountDownTimer
    private val startTime = 30 * 1000
    private val interval = 1000
    private var canResendCode = false
    private val emailVerification = EmailVerification()
    private var tryingsCounter = 0
    private var canCheckCode = true
    private val maxTryingsValue = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailVerificationBinding  = ActivityEmailVerificationBinding.inflate(LayoutInflater.from(this))
        setContentView(emailVerificationBinding.root)
        val intent = intent
        val userEmail = intent.getStringExtra("userEmail").toString()

        emailVerification.generateAndSendCode(userEmail)

        val timer = emailVerificationBinding.textViewTimer
        val resendCodeButton = emailVerificationBinding.resendCodeButton

        countDownTimer = object : CountDownTimer(startTime.toLong(), interval.toLong()) {
            override fun onFinish() {
                canResendCode = true
                resendCodeButton.setBackgroundColor(Color.DKGRAY)
            }

            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                var zero = ""
                if (seconds < 10) zero = "0"
                timer.setText("00 : $zero$seconds")
            }
        }.start()


        if (tryingsCounter == maxTryingsValue) {
            canCheckCode = false
        }

        val confirmButton = emailVerificationBinding.confirmButton
        var secondsBeforeNextTry : Long =  0
        val countDownTimerForNextTry = object : CountDownTimer(startTime.toLong(), interval.toLong()) {
            override fun onFinish() {
                canCheckCode = true
                tryingsCounter = 0
            }
            override fun onTick(millisUntilFinished: Long) {
                secondsBeforeNextTry = millisUntilFinished / 1000
            }
        }
        confirmButton.setOnClickListener() {
            if (canCheckCode) {
                tryingsCounter++
                val code = emailVerificationBinding.validationCodeEditText.text.toString()
                if (emailVerification.checkCode(code)) {

                } else {
                    Toast.makeText(this, "Wrong code!", Toast.LENGTH_LONG).show()
                }
            } else {
                countDownTimerForNextTry.start()
                Toast.makeText(this, "Please, wait $secondsBeforeNextTry before next try", Toast.LENGTH_LONG).show()
            }
        }
    }
}