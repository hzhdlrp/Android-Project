package com.example.myapplication

import com.example.myapplication.data.ApiServise.BalancesAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.ApiServise.Balance
import com.example.myapplication.data.ApiServise.DataRequests
import com.example.myapplication.data.ApiServise.SucsessfullAccountInfoResponse
import com.example.myapplication.data.ApiServise.UnsucsessfulAccountInfoResponce
import com.example.myapplication.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")

    private lateinit var recyclerView: RecyclerView
    private lateinit var balancesAdapter: BalancesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = intent.getParcelableExtra<User>("user")
        val apiKey = user?.getApiKey()
        val secretKey = user?.getSecretKey()
        val dataRequests = DataRequests()

        CoroutineScope(Dispatchers.IO).launch {
            if (apiKey != null && secretKey != null) {
                val result = dataRequests.getAccountInfo(apiKey, secretKey)
                withContext(Dispatchers.Main) {
                    if (result.isSucsessfull()) {
                        result as SucsessfullAccountInfoResponse

                        if (result.info != null) {
                            recyclerView = findViewById(R.id.recyclerViewBalances)
                            recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

                            val balances = mutableListOf<Balance>()

                            for (balance in result.info.balances) {
                                if (!balance.free.matches(Regex("^0*\\.0*$")) && !balance.free.matches(Regex("^0*\\.0*$"))) {
                                    balances.add(balance)
                                }
                            }
                            if (balances.isEmpty()) {
                                balances.add(Balance("", "you have not any asset", ""))
                            }

                            balancesAdapter = BalancesAdapter(balances)
                            recyclerView.adapter = balancesAdapter
                            recyclerView.addItemDecoration(
                                DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)
                            )
                            val animator = SlideInUpAnimator()
                            recyclerView.itemAnimator = animator
                        }


                    } else {
                        result as UnsucsessfulAccountInfoResponce
                        val info = result.info
                        Toast.makeText(this@HomeActivity, "Error: failed request, \n Error: $info", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Error: wrong keys", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
//view model scope