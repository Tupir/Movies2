package com.example.android.moviesremake;

/**
 * Created by PepovPC on 7/16/2017.
 * Adapter, ktory zobrazi jednotlive views
 */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.utils.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ForecastAdapterViewHolder> {

    private static final String TAG = DetailAdapter.class.getSimpleName();
    private ArrayList<String> trailers;
    private Context context;
    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String trailers);
    }

    public DetailAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;   // for Clicking
    }

    /**
     * Cache of the children views for a forecast list item.
     * Tato mala trieda obsahuje v sebe vsetko co ma obsahovat kazdy jednotlivy view
     * Vytvori a inicializuje ich
     * Tiez implementuje rozhranie, ktore umozni click (tak ako v main), ale tu sa nastavuju data (pozicia, movie)
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ConstraintLayout trailerView;
        public final TextView trailer;

       public ForecastAdapterViewHolder(View view) {
            super(view);
           trailerView = (ConstraintLayout) view.findViewById(R.id.trailerConst);
           trailer = (TextView) view.findViewById(R.id.trailer);
           view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {       // dokoncit
            int adapterPosition = getAdapterPosition();
//            Movie movie = moviesData.get(adapterPosition);
//            mClickHandler.onClick(movie);
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
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
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
        //forecastAdapterViewHolder.trailer.setText("Trailer "+Integer.toString(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }


}