package com.example.android.moviesremake;

/**
 * Created by PepovPC on 7/16/2017.
 * Adapter, ktory zobrazi jednotlive views
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesremake.utils.Movie;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.moviesremake.utils.HelperClass.getBitmap;
import static com.example.android.moviesremake.utils.HelperClass.getImageUri;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ForecastAdapterViewHolder> {

    private static final String TAG = FavoriteAdapter.class.getSimpleName();
    private ArrayList<Movie> moviesData;
    private Context context;
    private final ForecastAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(Movie weatherForDay);
    }

    public FavoriteAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        this.context = context;         // for Picasso
        mClickHandler = clickHandler;   // for Clicking
    }

    /**
     * Cache of the children views for a forecast list item.
     * Tato mala trieda obsahuje v sebe vsetko co ma obsahovat kazdy jednotlivy view
     * Vytvori a inicializuje ich
     * Tiez implementuje rozhranie, ktore umozni click (tak ako v main), ale tu sa nastavuju data (pozicia, movie)
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView obrazek;

       public ForecastAdapterViewHolder(View view) {
           super(view);
           obrazek = (ImageView) view.findViewById(R.id.tv_item_number);
           view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            //Movie movie = moviesData.get(adapterPosition);
            //Movie movie = mCursor.get
            //mClickHandler.onClick(movie);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     *
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.movie_grid_item, viewGroup, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     *
     * Vykresluje/nastavuje/vizualizuje data
     *
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);

//        byte[] bajt = mCursor.getBlob(FavoriteActivity.INDEX_MOVIE_IMAGE);
//        Bitmap bitmap = getBitmap(bajt);

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185//tWqifoYuwLETmmasnGHO7xBjEtt.jpg")
                .placeholder(R.drawable.no_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.no_image)
                .into(forecastAdapterViewHolder.obrazek);

//        Picasso.with(context)
//                .load(getImageUri(context, bitmap))
//                .placeholder(R.drawable.no_image)
//                .error(R.drawable.no_image)
//                .into(forecastAdapterViewHolder.obrazek);

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * This method is used to set the movies on a Adapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     */
    public void setCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}