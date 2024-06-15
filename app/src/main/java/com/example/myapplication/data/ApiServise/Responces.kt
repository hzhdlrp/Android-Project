package com.example.myapplication.data.ApiServise

data class AccountInfoResponse(
    val makerCommission: Int,
    val takerCommission: Int,
    val buyerCommission: Int,
    val sellerCommission: Int,
    val canTrade: Boolean,
    val canWithdraw: Boolean,
    val canDeposit: Boolean,
    val updateTime: Long,
    val accountType: String,
    val balances: List<Balance>,
    val permissions: List<String>
)

data class Balance(
    val asset: String,
    val free: String,
    val locked: String
)
