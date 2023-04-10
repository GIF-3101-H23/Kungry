package ca.ulaval.ima.mp.ui.restaurant_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ca.ulaval.ima.mp.utilities.RestaurantLight
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.max
import org.json.JSONObject
import org.json.JSONArray
import java.net.URL

private const val STARTING_KEY = 1

class RestaurantPagingSource: PagingSource<Int, RestaurantLight>(){
    private val restaurants: MutableList<RestaurantLight> = ArrayList()
    private var previousPage: Int? = null
    private var nextPage: Int? = null
    val client = OkHttpClient()
    override fun getRefreshKey(state: PagingState<Int, RestaurantLight>): Int? {
        TODO("Not yet implemented")
    }

    private fun run(numPage: Int): String? {
        val URL = "https://kungry.infomobile.app/api/v1/restaurant/?page=$numPage"
        val request = Request.Builder()
            .url(URL)
            .build()
        try {
            val response = client.newCall(request).execute()
            return response.body!!.string()
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            return null
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RestaurantLight> {
        val start = params.key?: STARTING_KEY

        val result = JSONObject(run(start)!!)
        val content = result.getJSONObject("content")
        nextPage = content.getInt("next")
        previousPage = content.getInt("previous")
        val array = restaurants.sortBy { restaurantLight ->restaurantLight.distance  }





        TODO("Not yet implemented")
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    private fun getRestaurants(prev: Int, next: Int){

    }
}