package ca.ulaval.ima.mp.ui.home.restaurant

import android.icu.text.Transliterator
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import java.time.LocalDate
import java.util.concurrent.CountDownLatch


class RestaurantService {

    private val restaurantApi = RestaurantApi("https://kungry.infomobile.app/api/v1")

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRestaurants  (

        onSuccess: (restaurants: List<ca.ulaval.ima.mp.utilities.RestaurantLight>) -> Unit,
        onError: (error: String) -> Unit
    ) {

        var page = 1 // Numéro de la page à récupérer
        for (i in 0 until pageSize)
        { val thread = Thread {
            try {

                val restaurants = restaurantApi.getRestaurants(page,pageSize)
                onSuccess(restaurants)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
            thread.start()
            thread.join()
            println("requete finwi")
            println("page:{$page}")
            page++
        }

    }


    companion object {
        const val pageSize = 1
    }




}

