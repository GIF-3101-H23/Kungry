package ca.ulaval.ima.mp.ui.RestaurantDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.databinding.FragmentItemBinding
import ca.ulaval.ima.mp.databinding.ReviewItemBinding
import ca.ulaval.ima.mp.utilities.Review
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate


class MyReviewRecyclerViewAdapter(
    private val values: List<Review>
) : RecyclerView.Adapter<MyReviewRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val date = binding.revDate
        val image = binding.revImage
        val name = binding.revName
        val comment = binding.revComment
        val ratingBar = binding.revRating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val formater = SimpleDateFormat("yyyy-MM-dd")
        holder.date.text = formater.parse(item.date)?.toString()
        holder.comment.text = item.comment
        holder.ratingBar.rating = item.stars.toFloat()
        if (item.image != null)
        {
            holder.image.visibility = View.VISIBLE
            Picasso.get().load(item.image).into(holder.image)
        }
        else{
            holder.image.visibility = View.INVISIBLE
        }
    }
}
