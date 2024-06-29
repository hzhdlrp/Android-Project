
import com.example.myapplication.data.ApiServise.AccountInfo
import com.example.myapplication.data.ApiServise.Prices
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BinanceApiService {

    @GET("api/v3/account")
    fun getAccountInfo(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("timestamp") timestamp: Long,
        @Query("signature") signature: String
    ): Call<AccountInfo>

    @GET("/api/v3/ticker/price")
    fun getPrices(): Call<Prices>
}
