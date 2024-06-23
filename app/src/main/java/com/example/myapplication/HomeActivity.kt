package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
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
                val result = dataRequests.getAccountInfo(apiKey, secretKey)
                withContext(Dispatchers.Main) {
                    if (result.isSucsessfull()) {
                        result as SucsessfullAccountInfoResponse

                        if (result.info != null) {
                            recyclerView = findViewById(R.id.recyclerViewBalances)
                            recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

                            val balances = mutableListOf<Balance>()
                            val add = balances.add(Balance("ASSET", "FREE", "LOCKED"))
                            for (balance in result.info.balances) {
//                                if (!balance.free.matches(Regex("^0*\\.0*$")) && !balance.free.matches(Regex("^0*\\.0*$"))) {
                                    balances.add(balance)
//                                }
                            }
                            if (balances.isEmpty()) {
                                balances.add(Balance("", "you have not any asset", ""))
                            }

                            balancesAdapter = BalancesAdapter(balances)
                            recyclerView.adapter = balancesAdapter
                            recyclerView.addItemDecoration(
                                DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)
                            )
                            val animator = DefaultItemAnimator()
                            recyclerView.itemAnimator = animator

                            // graphics

                            val data: MutableList<DataEntry> = ArrayList()

                            for (balance in result.info.balances) {
                                data.add(ValueDataEntry(balance.asset, balance.free.toFloat()))
                            }
                            val column = cartesian.column(data)

                            column.tooltip()
                                .titleFormat("{%X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0.0)
                                .offsetY(5.0)
                                .format("\${%Value}{groupsSeparator: }")


                            anyChartView.setChart(cartesian)


                        }
                    } else {
                        result as UnsucsessfulAccountInfoResponce
                        val info = result.info
                        Toast.makeText(this@HomeActivity, "Error: failed request, \n Error: $info", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Error: wrong keys", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}