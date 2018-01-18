package com.example.android.moviesremake.favorites;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesremake.MovieAdapter;
import com.example.android.moviesremake.R;
import com.example.android.moviesremake.realm.MovieRealm;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by PepovPC on 1/14/2018.
 */

public class FavoriteRealmAdapter extends RealmRecyclerViewAdapter<MovieRealm, FavoriteRealmAdapter.ForecastAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<MovieRealm> moviesData;
    private Context context;
    private final FavoriteRealmAdapter.ForecastAdapterOnClickHandler mClickHandler;
    private RealmChangeListener listener;

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(MovieRealm movieRealm);
    }


    public FavoriteRealmAdapter(@Nullable OrderedRealmCollection<MovieRealm> data, boolean autoUpdate,
                                Context context, FavoriteRealmAdapter.ForecastAdapterOnClickHandler clickHandler) {
        super(data, autoUpdate);
        this.context = context;         // for Picasso
        mClickHandler = clickHandler;   // for Clicking
        moviesData = data;
    }


    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.movie_grid_item, viewGroup, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        final MovieRealm movie = getItem(position);
        holder.data = movie;


        String weatherForThisDay = movie.getImage();

        System.out.println(weatherForThisDay);
        Picasso.with(context)
                .load(weatherForThisDay)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.obrazek);
    }

    @Override
    public int getItemCount() {
        if (null == moviesData) return 0;
        return moviesData.size();
    }


    public void setMoviesData(List<MovieRealm> data) {
        moviesData = data;
        notifyDataSetChanged();
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView obrazek;
        public MovieRealm data;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            obrazek = (ImageView) view.findViewById(R.id.tv_item_number);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieRealm movie = moviesData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }


}
