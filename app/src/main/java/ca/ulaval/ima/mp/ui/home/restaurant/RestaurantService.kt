package ca.ulaval.ima.mp.ui.home.restaurant

import android.icu.text.Transliterator
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import java.time.LocalDate


class RestaurantService {

    private val restaurantApi = RestaurantApi("https://kungry.infomobile.app/api/v1")

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRestaurants(
        onSuccess: (restaurants: List<ca.ulaval.ima.mp.utilities.RestaurantLight>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        Thread {
            try {
                val restaurants = restaurantApi.getRestaurants()

                onSuccess(restaurants)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }.start()
    }



    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun convertRestaurants(restaurantsJson: JSONArray): List<RestaurantLight> {

            return emptyList()

            }

        }



}

