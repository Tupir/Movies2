package com.example.android.moviesremake.details

/**
 * Created by PepovPC on 7/16/2017.
 * Adapter, ktory zobrazi jednotlive views
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.moviesremake.R
import com.example.android.moviesremake.retrofit.model.MovieRetrofitReview
import com.example.android.moviesremake.retrofit.model.MovieRetrofitTrailer
import java.util.*


class DetailAdapter(private val context: Context, private val mClickHandler: ForecastAdapterOnClickHandler)// for Clicking
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var reviews: List<MovieRetrofitReview>? = ArrayList()
    private var trailers: List<MovieRetrofitTrailer>? = ArrayList()


    interface ForecastAdapterOnClickHandler {
        fun onClick(trailer: String?)
    }


    inner class trailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val trailer: TextView

        init {
            trailer = itemView.findViewById(R.id.trailer) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val url = trailers!![adapterPosition].youtubeSource
            mClickHandler.onClick(url)
        }
    }

    inner class reviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView
        val text: TextView

        init {
            name = itemView.findViewById(R.id.name) as TextView
            text = itemView.findViewById(R.id.text) as TextView
        }

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(viewGroup.context)

        if (viewType == VIEW_TRAILERS) {
            val v = layoutInflater.inflate(R.layout.trailer_item, viewGroup, false)

            return trailerViewHolder(v)
        } else {
            val v = layoutInflater.inflate(R.layout.review_item, viewGroup, false)

            return reviewViewHolder(v)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            VIEW_TRAILERS -> {
                (holder as trailerViewHolder).trailer.text = trailers!![position].trailerName
                val str = trailers!![position].trailerName
                println(str)
            }
            VIEW_REVIEWS -> {
                (holder as reviewViewHolder).name.text = reviews!![position].autorName
                holder.text.text = reviews!![position].getAutorComment()
            }
            else -> println("Error in onBindViewHolder method")
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (reviews!!.size == 0 && trailers!!.size != 0)
            VIEW_TRAILERS
        else
            VIEW_REVIEWS
    }


    override fun getItemCount(): Int {
        return if (reviews == null || reviews!!.size == 0)
            trailers!!.size
        else
            reviews!!.size
    }


    fun setReviewData(data: List<MovieRetrofitReview>?) {
        if (data == null) {
            return
        }
        reviews = data
        notifyDataSetChanged()
    }

    fun setTrailerData(data: List<MovieRetrofitTrailer>?) {
        if (data == null) {
            return
        }
        trailers = data
        notifyDataSetChanged()
    }

    companion object {

        private val TAG = DetailAdapter::class.java.simpleName
        private val VIEW_TRAILERS = 0
        private val VIEW_REVIEWS = 1
    }

}