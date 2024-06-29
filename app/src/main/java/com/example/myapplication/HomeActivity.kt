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
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.myapplication.data.ApiServise.Balance
import com.example.myapplication.data.ApiServise.BalancesAdapter
import com.example.myapplication.data.ApiServise.DataRequests
import com.example.myapplication.data.ApiServise.SuccessfulAccountInfoResponse
import com.example.myapplication.data.ApiServise.UnsuccessfulAccountInfoResponse
import com.example.myapplication.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        val cartesian: Cartesian = AnyChart.column()


        cartesian.animation(true)
        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title("Assets")
        cartesian.yAxis(0).title("Amount (free)")


        CoroutineScope(Dispatchers.IO).launch {
            if (apiKey != null && secretKey != null) {
                while (true) {
                    val result = dataRequests.getAccountInfo(apiKey, secretKey)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful()) {
                            result as SuccessfulAccountInfoResponse

                            if (result.info != null) {
                                recyclerView = findViewById(R.id.recyclerViewBalances)
                                recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

                                val balances = mutableListOf<Balance>()
                                balances.add(Balance("ASSET", "FREE", "LOCKED"))
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
                                    DividerItemDecoration(
                                        this@HomeActivity,
                                        DividerItemDecoration.VERTICAL
                                    )
                                )
                                val animator = DefaultItemAnimator()
                                recyclerView.itemAnimator = animator

                                // graphics

                                val data: MutableList<DataEntry> = ArrayList()

                                for (balance in result.info.balances) {
                                    if (!balance.free.matches(Regex("^0*\\.0*$")) && !balance.free.matches(Regex("^0*\\.0*$"))) {
                                        data.add(ValueDataEntry(balance.asset, balance.free.toFloat()))
                                    }
                                }

                                if (data.isEmpty()) {
                                    cartesian.background().enabled(true)
                                    cartesian.background().fill("#FFFFFF")

                                    val noDataLabel = cartesian.label(0)
                                    noDataLabel.enabled(true)
                                    noDataLabel.text("No data")
                                    noDataLabel.position("center")
                                    noDataLabel.anchor("center")

                                } else {

                                    val column = cartesian.column(data)

                                    column.tooltip()
                                        .titleFormat("{%X}")
                                        .position(Position.CENTER_BOTTOM)
                                        .anchor(Anchor.CENTER_BOTTOM)
                                        .offsetX(0.0)
                                        .offsetY(5.0)
                                        .format("\${%Value}{groupsSeparator: }")
                                }

                                anyChartView.setChart(cartesian)
                            }

                        } else {
                            result as UnsuccessfulAccountInfoResponse
                            val info = result.info
                            Toast.makeText(
                                this@HomeActivity,
                                "Error: failed request, \n Error: $info",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    delay(30000)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Error: wrong keys", Toast.LENGTH_LONG).show()
                }
            }
        }

        val pricesButton = findViewById<Button>(R.id.assets_prices_button)
        pricesButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, PricesActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }
    }


}