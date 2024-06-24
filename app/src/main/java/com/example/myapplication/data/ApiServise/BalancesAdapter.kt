package com.example.myapplication.data.ApiServise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
class BalancesAdapter(private val balances: List<Balance>) : RecyclerView.Adapter<BalancesAdapter.BalanceViewHolder>() {

    class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewLocked: TextView = itemView.findViewById(R.id.textViewLocked)
        val textViewAsset: TextView = itemView.findViewById(R.id.textViewAsset)
        val textViewFree: TextView = itemView.findViewById(R.id.textViewFree)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balance, parent, false)
        return BalanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        val balance = balances[position]
        holder.textViewAsset.text = balance.asset
        holder.textViewFree.text = balance.free
        holder.textViewLocked.text = balance.locked
    }

    override fun getItemCount(): Int = balances.size
}