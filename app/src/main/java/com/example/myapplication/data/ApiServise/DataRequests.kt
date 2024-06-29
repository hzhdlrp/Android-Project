package com.example.myapplication.data.ApiServise


import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class DataRequests {
    private fun generateSignature(secretKey: String, data: String): String {
        val sha256HMAC = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        sha256HMAC.init(secretKeySpec)
        return sha256HMAC.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    suspend fun getAccountInfo(apiKey: String, secretKey: String): MyAccountInfoResponse {
        return try {
            val timestamp = System.currentTimeMillis()
            val queryString = "timestamp=$timestamp"
            val signature = generateSignature(secretKey, queryString)

            val response = RetrofitClient.instance.getAccountInfo(apiKey, timestamp, signature)

            val gotResponse = response.execute()
            if (gotResponse.isSuccessful) {
//                val body = gotResponse.body()?.string()
//                val data = Gson().fromJson(body, AccountInfo::class.java)
                val successfullResponse = SuccessfulAccountInfoResponse(gotResponse.body())
                successfullResponse
            } else {
                val unsuccessfulResponse = UnsuccessfulAccountInfoResponse("Request failed: ${gotResponse.message()}, code: ${gotResponse.code()}")
                unsuccessfulResponse

            }
        } catch (e: Exception) {
            val unsuccessfulResponse = UnsuccessfulAccountInfoResponse("Request failed: ${e.message}")
            unsuccessfulResponse
        }
    }

    suspend fun getPrices(): PricesResponse {
        return try {
            val response = RetrofitClient.instance.getPrices()
            val gotResponse = response.execute()
            if (gotResponse.isSuccessful) {
                val successfulPricesResponse = SuccessfulPricesResponse(gotResponse.body())
                successfulPricesResponse
            } else {
                val unsuccessfulAccountInfoResponse = UnsuccessfulPricesResponse("Request failed: ${gotResponse.message()}, code: ${gotResponse.code()}")
                unsuccessfulAccountInfoResponse
            }
        } catch (e: Exception) {
            val unsuccessfulPricesResponse = UnsuccessfulPricesResponse("Request failed: ${e.message}")
            unsuccessfulPricesResponse
        }
    }
}

sealed interface MyAccountInfoResponse {
    open fun isSuccessful() : Boolean {
        return true
    }
}

class SuccessfulAccountInfoResponse(val info: AccountInfo?) : MyAccountInfoResponse {
    override fun isSuccessful() : Boolean {
        return info != null
    }
}
class UnsuccessfulAccountInfoResponse(val info : String) : MyAccountInfoResponse {
    override fun isSuccessful() : Boolean {
        return false
    }
}

sealed interface PricesResponse {
    open fun isSuccessful() : Boolean {
        return true
    }
}

class SuccessfulPricesResponse(val info : Prices?) : PricesResponse {
    override fun isSuccessful(): Boolean {
        return info != null
    }
}

class UnsuccessfulPricesResponse(val info: String) : PricesResponse {
    override fun isSuccessful(): Boolean {
        return false
    }
}
