package com.example.myapplication.data.ApiServise


import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class DataRequests {
    fun generateSignature(secretKey: String, data: String): String {
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
                val sucsessfullResponce = SucsessfullAccountInfoResponse(gotResponse.body())
                sucsessfullResponce
            } else {
                val unsucsessfulResponce = UnsucsessfulAccountInfoResponce("Request failed: ${gotResponse.message()}, code: ${gotResponse.code()}")
                unsucsessfulResponce
            }
        } catch (e: Exception) {
            val unsucsessfulResponce = UnsucsessfulAccountInfoResponce("Request failed: ${e.message}")
            unsucsessfulResponce
        }
    }
}

sealed interface MyAccountInfoResponse {
    open fun isSucsessfull() : Boolean {
        return true
    }
}

class SucsessfullAccountInfoResponse(val info: AccountInfoResponse?) : MyAccountInfoResponse {
    override fun isSucsessfull() : Boolean {
        return info != null
    }
}
class UnsucsessfulAccountInfoResponce(val info : String) : MyAccountInfoResponse {
    override fun isSucsessfull() : Boolean {
        return false
    }
}