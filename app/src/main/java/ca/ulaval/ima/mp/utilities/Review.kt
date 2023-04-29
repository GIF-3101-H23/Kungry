package ca.ulaval.ima.mp.utilities

import org.json.JSONArray
import org.json.JSONObject



data class Creator(val firstName: String, val lastName: String)


data class Review(val id:Int, val creator: Creator, val stars:Int, val image:String?,
            val comment:String, val date: String) {

    companion object {
        fun createReviews(restos: JSONArray): ArrayList<Review>
        {
            val reviews = ArrayList<Review>()
            val lengh = restos.length() - 1
            for(i in 0..lengh)
            {
                reviews.add(createReview(restos.getJSONObject(i)))
            }
            return reviews
        }

        fun createReview(resto: JSONObject): Review
        {
            val creator = resto.getJSONObject("creator")
            return Review(resto.getInt("id"), Creator(creator.getString("first_name"), creator.getString("last_name")),
                        resto.getInt("stars"), resto.getString("image"), resto.getString("comment"), resto.getString("date") )
        }
    }
}