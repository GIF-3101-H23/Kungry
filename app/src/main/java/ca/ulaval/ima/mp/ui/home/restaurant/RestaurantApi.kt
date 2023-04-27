package ca.ulaval.ima.mp.ui.home.restaurant
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ca.ulaval.ima.mp.ui.home.restaurant.*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class RestaurantApi(private val baseUrl: String) {

    private val httpClient = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRestaurants(page: Int = 1,pageSize: Int = 20): List<RestaurantLight> {
        val restaurants = mutableListOf<RestaurantLight>()
        var hasNextPage = true
        var nextPage = page
        while (hasNextPage) {
            val request = Request.Builder()
                .url("$baseUrl/restaurant?page=$nextPage&page_size=$pageSize")
                .build()
            println("Connecting to $baseUrl...")
            try {
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        println("Failed to connect to $baseUrl. Unexpected code ${response.code}")
                        throw IOException("Unexpected code ${response.code}")
                    } else {
                        println("Successfully connected to $baseUrl")
                        val content = JSONObject(response.body?.string()).getJSONObject("content")
                        val count = content.getInt("count")
                        val next = content.optInt("next")
                        val previous = content.optInt("previous")
                        val restaurantsJson = content.getJSONArray("results")
                        val batchRestaurants = RestaurantService.convertRestaurants(restaurantsJson)
                        restaurants.addAll(batchRestaurants)
                        if (next == null) {
                            hasNextPage = false
                        } else {
                            nextPage = next
                        }
                    }
                }
            } catch (e: IOException) {
                println("Error connecting to $baseUrl: ${e.message}")
                throw e
            }
        }

        return restaurants
    }


}

