package ca.ulaval.ima.mp.utilities

import org.json.JSONArray
import org.json.JSONObject


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
    val cuisine: MutableList<Cuisine>,
    val type: Type,
    val count: Int,
    val average: Int,
    val image: String,
    val distance: Double,
    val location: Location


) {
    companion object{
        fun createRestaurants(array: JSONArray): ArrayList<RestaurantLight>
        {
            val restaurants = ArrayList<RestaurantLight>()
            val lengh = array.length() - 1
            for(i in 0..lengh)
            {
                restaurants.add(createRestaurant(array.getJSONObject(i)))
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
            val cuisine = createCuisine(objects.getJSONArray("cuisine"))
            val image = objects.getString("image")
            val location = Location(objects.getJSONObject("location").getDouble("latitude"),objects.getJSONObject("location").getDouble("longitude"))
            return RestaurantLight(id, name, cuisine, type, count, average, image, -1.0, location)

        }
        private fun createCuisine(cuisines : JSONArray): MutableList<Cuisine>
        {
            val array = ArrayList<Cuisine>()
            val lengh = cuisines.length()-1
            for(i in 0..lengh)
            {
                array.add(Cuisine(cuisines.getJSONObject(i).getInt("id"), cuisines.getJSONObject(i).getString("name")))
            }
            return array
        }
    }


}