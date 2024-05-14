package alpaca

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import database.DatabaseHandler
import net.jacobpeterson.alpaca.AlpacaAPI
import net.jacobpeterson.alpaca.model.util.apitype.TraderAPIEndpointType
import net.jacobpeterson.alpaca.openapi.trader.model.Order
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

val client = OkHttpClient()
val api = AlpacaAPI("", TraderAPIEndpointType.PAPER, client)

fun canConnect(oauth: String?): Boolean {
    if (oauth == null) return false
    try {
        AlpacaAPI(oauth, TraderAPIEndpointType.PAPER, client)
            .trader()
            .assets()
            .getV2Assets(null, null, null, null)
        return true
    } catch (e: Exception) {
        print(e)
        return false
    }
}

fun getOrders(guild: Long): List<Order> {
    return AlpacaAPI(DatabaseHandler.getToken(guild), TraderAPIEndpointType.PAPER)
        .trader()
        .orders()
        .getAllOrders("all", 10, null, null, null, null, null, null)
}

fun oauthExchange(token: String): String? {
    val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
    val params = "grant_type=authorization_code&code=$token&client_id=${System.getenv("CLIENT_ID")}&client_secret=${System.getenv("CLIENT_SECRET")}&redirect_uri=https://127.0.0.1"
    val requestBody = params.toRequestBody(mediaType)
    val request = Request.Builder()
        .post(requestBody)
        .url("https://api.alpaca.markets/oauth/token")
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            print(response.body!!.string())
            return null
        }
        return ObjectMapper()
            .readValue(response.body!!.string().toByteArray(), OauthResponseObject::class.java)
            .accessToken
    }
}

data class OauthResponseObject(@JsonProperty("access_token") val accessToken: String?,
                               @JsonProperty("token_type") val tokenType: String?,
                               @JsonProperty("scope") val scope: String?)