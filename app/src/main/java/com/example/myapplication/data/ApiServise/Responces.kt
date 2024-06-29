package com.example.myapplication.data.ApiServise

data class AccountInfo(
    val makerCommission: Int,
    val takerCommission: Int,
    val buyerCommission: Int,
    val sellerCommission: Int,
    val commissionRates: CommissionRates,
    val canTrade: Boolean,
    val canWithdraw: Boolean,
    val canDeposit: Boolean,
    val brokered: Boolean,
    val requireSelfTradePrevention: Boolean,
    val preventSor: Boolean,
    val updateTime: Long,
    val accountType: String,
    val balances: List<Balance>,
    val permissions: List<String>,
    val uid: Long
)

data class CommissionRates(
    val maker: String,
    val taker: String,
    val buyer: String,
    val seller: String
)

data class Balance(
    val asset: String,
    val free: String,
    val locked: String
)

data class Prices(
    val prices: List<Price>
)

data class Price(
    val symbol: String,
    val price: String
)