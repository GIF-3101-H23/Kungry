package ca.ulaval.ima.mp.ui.restaurant_list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.databinding.FragmentItemBinding

import ca.ulaval.ima.mp.ui.restaurant_list.placeholder.PlaceholderContent.PlaceholderItem
import ca.ulaval.ima.mp.utilities.RestaurantLight
import com.squareup.picasso.Picasso


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyRestaurantRecyclerViewAdapter(
    private val values: List<RestaurantLight>
) : RecyclerView.Adapter<MyRestaurantRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Picasso.get().load(item.image).into(holder.imageView)
        holder.distanceView.text = "${item.distance} km"
        holder.countView.text = "(${item.count})"
        holder.titleView.text = item.name
        holder.typeView.text = item.type.toString()
        holder.ratingBar.rating = item.average.toFloat()

        //holder.idView.text = item.id
        //holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //val idView: TextView = binding.itemNumbe
        val ratingBar = binding.ratingBar2
        val imageView = binding.imageView
        val titleView = binding.itemTitle
        val typeView = binding.itemType
        val countView = binding.itemCounts
        val distanceView = binding.itemDistance
        //val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

}