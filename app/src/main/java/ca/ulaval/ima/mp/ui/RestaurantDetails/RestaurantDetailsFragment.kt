package ca.ulaval.ima.mp.ui.RestaurantDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.ui.restaurant_list.MyRestaurantRecyclerViewAdapter
import ca.ulaval.ima.mp.utilities.RestaurantLight
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantDetailsFragment(val id:Int) : Fragment() {
    private lateinit var detail_title: TextView
    private lateinit var detail_distance: TextView
    private lateinit var detail_image: ImageView
    private lateinit var detail_type: TextView
    private lateinit var detail_rating_count: TextView
    private lateinit var detail_phone: TextView
    private lateinit var detail_link: TextView
    private lateinit var rating_bar: RatingBar
    private lateinit var detail_eval_count: TextView
    private lateinit var comment_list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        runPages()
        super.onCreate(savedInstanceState)

    }

    private fun runPages() {
        val baseUrl = "https://kungry.infomobile.app/api/v1/restaurant/$id/"
        val mQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(
            Request.Method.GET, baseUrl, null,
            { response ->
                //val brands : MutableList<Brand> = ArrayList()
                val content = response.getJSONObject("content")
            },
            { error ->
                error.printStackTrace()
            }
        )
        mQueue.add(request)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_restaurant_details, container, false)
        detail_image = root.findViewById(R.id.detail_image)
        detail_distance = root.findViewById(R.id.detail_distance)
        detail_link = root.findViewById(R.id.detail_link)
        detail_type = root.findViewById(R.id.detail_type)
        detail_phone = root.findViewById(R.id.detail_phone)
        detail_eval_count = root.findViewById(R.id.detail_eval_count)
        detail_title = root.findViewById(R.id.detail_title)
        detail_rating_count = root.findViewById(R.id.detail_rating_count)
        return root
    }


}