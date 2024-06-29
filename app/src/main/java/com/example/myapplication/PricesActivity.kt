package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.ApiService.PricesAdapter
import com.example.myapplication.data.ApiServise.DataRequests
import com.example.myapplication.data.ApiServise.Price
import com.example.myapplication.data.ApiServise.PricesResponse
import com.example.myapplication.data.ApiServise.SuccessfulPricesResponse
import com.example.myapplication.data.ApiServise.UnsuccessfulPricesResponse
import com.example.myapplication.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class PricesActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")

    private lateinit var recyclerView: RecyclerView
    val user = intent.getParcelableExtra<User>("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)

        recyclerView = findViewById(R.id.recyclerViewPrices)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        recyclerView.itemAnimator = DefaultItemAnimator()

        val dataRequests = DataRequests()

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val result = dataRequests.getPrices()
                withContext(Dispatchers.Main) {
                    handlePricesResponse(result)
                }
                delay(30000)
            }
        }

        val accountInfoButton = findViewById<Button>(R.id.account_info_button)
        accountInfoButton.setOnClickListener {
            val intent = Intent(this@PricesActivity, HomeActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }
    }

    private fun handlePricesResponse(result: PricesResponse) {
        if (result.isSuccessful()) {
            val successfulResult = result as SuccessfulPricesResponse
            val prices = mutableListOf<Price>()
            prices.add(Price("ASSET", "PRICE"))

            successfulResult.info?.prices?.let { prices.addAll(it) }

            val pricesAdapter = PricesAdapter(prices)
            recyclerView.adapter = pricesAdapter

        } else {
            val unsuccessfulResult = result as UnsuccessfulPricesResponse
            val info = unsuccessfulResult.info
            Toast.makeText(
                this,
                "Error: failed request, \n Error: $info",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}