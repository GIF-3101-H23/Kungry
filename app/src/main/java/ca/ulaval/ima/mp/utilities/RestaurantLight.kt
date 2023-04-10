package ca.ulaval.ima.mp.utilities

import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects

data class Cuisine(val id: Int, val name: String){
}

enum class Type{
    RESTO,
    BAR,
    SNACK
}
data class Location(val latitude: Double, val longitude: Double){}

data class RestaurantLight(
    val id: Int,
    val name: String,
    val cuisine: Cuisine,
    val type: Type,
    val count: Int,
    val average: Int,
    val image: String,
    val distance: Float,
    val location: Location


) {
    companion object{
        fun createRestaurants(array: JSONArray): ArrayList<RestaurantLight>
        {
            val restaurants = ArrayList<RestaurantLight>()
            val lengh = array.length() - 1
            for(i in 0..lengh)
            {

            }
            return restaurants
        }

        fun createRestaurant(objects: JSONObject): RestaurantLight
        {
            val id = objects.getInt("id")
            val name = objects.getString("name")
            val type = when(objects.getString("type")){
                "RESTO"-> Type.RESTO
                "SNACK"-> Type.SNACK
                "BAR"-> Type.BAR
                else -> Type.BAR

            }
            val count = objects.getInt("review_count")
            val average = objects.getInt("review_average")
            val cuisine = Cuisine(objects.getJSONObject("cuisine").getInt("id"), objects.getJSONObject("cuisine").getString("name"))
            val image = objects.getString("image")
            val location = Location(objects.getJSONObject("Location").getDouble("latitude"),objects.getJSONObject("Location").getDouble("longitude"))

        }
    }


}