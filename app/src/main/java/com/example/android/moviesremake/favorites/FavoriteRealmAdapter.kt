package com.example.android.moviesremake.favorites

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.android.moviesremake.R
import com.example.android.moviesremake.realm.MovieRealm
import com.squareup.picasso.Picasso
import io.realm.OrderedRealmCollection
import io.realm.RealmChangeListener
import io.realm.RealmRecyclerViewAdapter

/**
 * Created by PepovPC on 1/14/2018.
 */

class FavoriteRealmAdapter(data: OrderedRealmCollection<MovieRealm>?, autoUpdate: Boolean,
                           private val context: Context,
                           private val mClickHandler: FavoriteRealmAdapter.ForecastAdapterOnClickHandler) : RealmRecyclerViewAdapter<MovieRealm,
        FavoriteRealmAdapter.ForecastAdapterViewHolder>(data, autoUpdate) {


    private var moviesData: List<MovieRealm>? = null
    private val listener: RealmChangeListener<*>? = null

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    interface ForecastAdapterOnClickHandler {
        fun onClick(movieRealm: MovieRealm)
    }


    init {
        moviesData = data
    }// for Picasso
    // for Clicking


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ForecastAdapterViewHolder {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.movie_grid_item, viewGroup, false)

        view.isFocusable = true

        return ForecastAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastAdapterViewHolder, position: Int) {

        val movie = getItem(position)
        holder.data = movie


        val weatherForThisDay = movie!!.image

        println(weatherForThisDay)
        Picasso.with(context)
                .load(weatherForThisDay)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.obrazek)
    }

    override fun getItemCount(): Int {
        return if (null == moviesData) 0 else moviesData!!.size
    }


    fun setMoviesData(data: List<MovieRealm>) {
        moviesData = data
        notifyDataSetChanged()
    }


    inner class ForecastAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val obrazek: ImageView
        var data: MovieRealm? = null

        init {
            obrazek = view.findViewById(R.id.tv_item_number) as ImageView
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val movie = moviesData!![adapterPosition]
            mClickHandler.onClick(movie)
        }
    }


}
