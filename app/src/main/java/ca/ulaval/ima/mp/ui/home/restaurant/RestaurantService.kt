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
        onSuccess: (restaurants: List<RestaurantLight>) -> Unit,
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNearbyRestaurants(
        position: Location,
        maxDistance: Double,
        onSuccess: (restaurants: List<RestaurantLight>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        val restaurantsList = mutableListOf<RestaurantLight>()
        RestaurantService().getRestaurants(
            onSuccess = { restaurants ->
                for (restaurant in restaurants) {
                    val restaurantLocation = Location("").apply {
                        latitude = restaurant.location.latitude
                        longitude = restaurant.location.longitude
                    }
                    val distance = position.distanceTo(restaurantLocation) / 1000.0
                    if (distance <= maxDistance) {
                        restaurantsList.add(restaurant)
                    }
                }
                onSuccess(restaurantsList)
            },
            onError = onError
        )
    }


    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun convertRestaurants(restaurantsJson: JSONArray): List<RestaurantLight> {
            println("la taille")
            println(restaurantsJson.length())
            for (i in 0 until restaurantsJson.length()) {
                val restaurantJson = restaurantsJson.getJSONObject(i)
                val name = restaurantJson.getString("name")
                val id = restaurantJson.getString("id")
                val cuisine = restaurantJson.getString("cuisine")
                val type = restaurantJson.getString("type")
                val review_count = restaurantJson.getString("review_count")
                val review_average = restaurantJson.getString("review_average")
                val image = restaurantJson.getString("image")
                val distance = restaurantJson.getString("distance")
                val location = restaurantJson.getString("location")
                val lattitude = restaurantJson.getJSONObject("location").getDouble("latitude")
                val longitude = restaurantJson.getJSONObject("location").getDouble("longitude")

                println(name)
                println(id)
                println(cuisine)
                println(type)
                println(review_count)
                println(review_average)
                println(image)
                println(distance)
                println(location)
                println(lattitude)
                println(longitude)

            }
            return emptyList()
        }


    }
}

