package ca.ulaval.ima.mp.ui.home.restaurant

import java.time.LocalDate

data class RestaurantLight(
    val id: Int,
    val name: String,
    val cuisine: List<Cuisine>,
    val type: String,
    val review_count: Int,
    val review_average: Double,
    val image: String?,
    val distance: String,
    val location: Location
)
data class Location(val latitude: Double, val longitude: Double){}
data class Cuisine(
    val id: Int,
    val name: String
)

data class Creator(
    val id: Int,
    val name: String
)

enum class RestaurantType {
    RESTO,
    BAR,
    SNACK
}


