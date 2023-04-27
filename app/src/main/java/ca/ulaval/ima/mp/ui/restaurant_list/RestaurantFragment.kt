package ca.ulaval.ima.mp.ui.restaurant_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.utilities.RestaurantLight
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject
import java.io.IOException

/**
 * A fragment representing a list of Items.
 */
class RestaurantFragment : Fragment() {

    private var columnCount = 1
    private var array: MutableList<RestaurantLight> = ArrayList()

    //val client = OkHttpClient()
    private var previousPage: Int? = 1
    private var nextPage: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        previousPage?.let { runPages(it) }
        super.onCreate(savedInstanceState)
    }
    private fun runPages(numPage: Int?) {
        if (numPage != null)
        {
            val baseUrl = "https://kungry.infomobile.app/api/v1/restaurant/?page=$numPage"
            val mQueue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(
                Request.Method.GET, baseUrl, null,
                { response ->
                    //val brands : MutableList<Brand> = ArrayList()
                    val content = response.getJSONObject("content")
                    try {
                        previousPage = content.getInt("previous")
                    } catch (e: Exception) {
                        previousPage = null
                    }
                    try {
                        nextPage = content.getInt("next")
                    } catch (e: Exception) {
                        nextPage = null
                    }
                    array = RestaurantLight.createRestaurants(content.getJSONArray("results"))
                    val recycler = view?.findViewById<RecyclerView>(R.id.list)

                    if (recycler != null) {
                        recycler.adapter = MyRestaurantRecyclerViewAdapter(array)
                    }
                },
                { error ->
                    error.printStackTrace()
                }
            )
            mQueue.add(request)
        }

    }
    /*private fun runPage(numPage: Int?) {
        if (numPage != null) {
            val URL = "https://kungry.infomobile.app/api/v1/restaurant/?page=$numPage"
            val request = Request.Builder()
                .url(URL)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = JSONObject(response.body!!.string())
                    val content = result.getJSONObject("content")

                    try {
                        previousPage = content.getInt("previous")
                    } catch (e: Exception) {
                        previousPage = null
                    }
                    try {
                        nextPage = content.getInt("next")
                    } catch (e: Exception) {
                        nextPage = null
                    }

                    array = RestaurantLight.createRestaurants(content.getJSONArray("results"))
                    val recycler = view?.findViewById<RecyclerView>(R.id.list)

                    if (recycler != null) {
                        recycler.adapter = MyRestaurantRecyclerViewAdapter(array)
                    }

                }
            })
        }
    }*/

    override fun onResume() {
        previousPage?.let { runPages(it) }
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyRestaurantRecyclerViewAdapter(array)
                addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        //val pos = (adapter as Any).itemCount
                        val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        println(lastItem)
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }
        }
        return view
    }
}