package com.example.myapplication.data.ApiService

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.ApiServise.Price

data class Price(val symbol: String, val price: String)

class PricesAdapter(private val prices: MutableList<Price>) : RecyclerView.Adapter<PricesAdapter.PriceViewHolder>() {

    class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSymbol: TextView = itemView.findViewById(R.id.textViewSymbol)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_price, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val price = prices[position]
        holder.textViewSymbol.text = price.symbol
        holder.textViewPrice.text = price.price
    }

    override fun getItemCount(): Int = prices.size
}