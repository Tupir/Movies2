package com.example.android.moviesremake

/**
 * Created by PepovPC on 7/16/2017.
 * Adapter, ktory zobrazi jednotlive views
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.android.moviesremake.retrofit.model.MovieRetrofit
import com.squareup.picasso.Picasso


class MovieAdapter(private val context: Context, private val mClickHandler: adapterOnClickHandler)// for Picasso
// for Clicking
    : RecyclerView.Adapter<MovieAdapter.ForecastAdapterViewHolder>() {
    private var moviesData: List<MovieRetrofit>? = null

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    interface adapterOnClickHandler {
        fun onClick(movieRetrofit: MovieRetrofit)
    }

    /**
     * Cache of the children views for a forecast list item.
     * Tato mala trieda obsahuje v sebe vsetko co ma obsahovat kazdy jednotlivy view
     * Vytvori a inicializuje ich
     * Tiez implementuje rozhranie, ktore umozni click (tak ako v main), ale tu sa nastavuju data (pozicia, movie)
     */
    inner class ForecastAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val obrazek: ImageView

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

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ForecastAdapterViewHolder {
        val context = viewGroup.context
        val layoutIdForListItem = R.layout.movie_grid_item
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately)
        return ForecastAdapterViewHolder(view)
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     * contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     *
     * Vykresluje/nastavuje/vizualizuje data
     */
    override fun onBindViewHolder(forecastAdapterViewHolder: ForecastAdapterViewHolder, position: Int) {

        val weatherForThisDay = moviesData!![position].getImage()
        Picasso.with(context)
                .load(weatherForThisDay)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(forecastAdapterViewHolder.obrazek)
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    override fun getItemCount(): Int {
        return if (null == moviesData) 0 else moviesData!!.size
    }

    /**
     * This method is used to set the movies on a Adapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param data The new weather data to be displayed.
     */
    fun setMoviesData(data: List<MovieRetrofit>) {
        moviesData = data
        notifyDataSetChanged()
    }

}