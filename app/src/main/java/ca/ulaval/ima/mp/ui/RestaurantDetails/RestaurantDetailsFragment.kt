package ca.ulaval.ima.mp.ui.RestaurantDetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.ui.restaurant_list.MyRestaurantRecyclerViewAdapter
import ca.ulaval.ima.mp.utilities.Location
import ca.ulaval.ima.mp.utilities.RestaurantLight
import ca.ulaval.ima.mp.utilities.Review
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantDetailsFragment(val ide:Int) : Fragment(), OnMapReadyCallback {
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
    private lateinit var arrayRev : ArrayList<Review>
    private lateinit var detail_map: MapView
    private lateinit var location: JSONObject
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        runPages()

        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    private fun runPages() {
        val baseUrl = "https://kungry.infomobile.app/api/v1/restaurant/$ide/"
        val mQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(
            Request.Method.GET, baseUrl, null,
            { response ->
                //val brands : MutableList<Brand> = ArrayList()
                val content = response.getJSONObject("content")
                arrayRev = Review.createReviews(content.getJSONArray("reviews"))
                detail_distance.text = "${content.getDouble("distance")} km"
                Picasso.get().load(content.getString("image")).into(detail_image)
                detail_title.text = content.getString("name")
                detail_phone.text = content.getString("phone_number")
                rating_bar.rating = content.getDouble("review_average").toFloat()
                detail_type.text = "${content.getString("type")}-${content.getJSONArray("cuisine").getJSONObject(0).getString("name")}"
                detail_link.setOnClickListener {

                }
                detail_eval_count.text = content.getString("review_count")
                detail_rating_count.text = content.getString("review_count")
                location = content.getJSONObject("location")
                comment_list.adapter = MyReviewRecyclerViewAdapter(arrayRev)
                val place = LatLng(location.getDouble("latitude"), location.getDouble("longitude"))
                //googleMap = detail_map.getMapAsync(this) as GoogleMap
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.addMarker(MarkerOptions().position(place).title("HERE!!"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,15f))

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
        detail_map = root.findViewById(R.id.mapView2)
        detail_map.onCreate(savedInstanceState)
        detail_map.onResume()
        detail_map.getMapAsync(this)
        detail_image = root.findViewById(R.id.detail_image)
        detail_distance = root.findViewById(R.id.detail_distance)
        detail_link = root.findViewById(R.id.detail_link)
        detail_type = root.findViewById(R.id.detail_type)
        detail_phone = root.findViewById(R.id.detail_phone)
        detail_eval_count = root.findViewById(R.id.detail_eval_count)
        detail_title = root.findViewById(R.id.detail_title)
        detail_rating_count = root.findViewById(R.id.detail_rating_count)
        rating_bar = root.findViewById(R.id.ratingBar)
        comment_list = root.findViewById(R.id.comment_list)

        comment_list.layoutManager = LinearLayoutManager(context)
        return root
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }


}