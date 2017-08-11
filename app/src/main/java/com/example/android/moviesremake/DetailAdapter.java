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

import static android.media.CamcorderProfile.get;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ForecastAdapterViewHolder> {

    private static final String TAG = DetailAdapter.class.getSimpleName();
    private ArrayList<String> trailers;
    private Context context;
    private final ForecastAdapterOnClickHandler mClickHandler;
    private static final int VIEW_TRAILERS = 0;
    private static final int VIEW_REVIEWS = 1;


    class ViewHolder0 extends RecyclerView.ViewHolder {
        public ViewHolder0(View itemView){
            super(itemView);
        }
    }



    public interface ForecastAdapterOnClickHandler {
        void onClick(String trailer);
    }

    public DetailAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;   // for Clicking
    }



    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public final ConstraintLayout trailerView;
        public final TextView trailer;
        public final ImageView image;

       public ForecastAdapterViewHolder(View view) {
            super(view);
           //trailerView = (ConstraintLayout) view.findViewById(R.id.trailerConst);
           trailer = (TextView) view.findViewById(R.id.trailer);
           image = (ImageView) view.findViewById(R.id.imageView);
           view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String url = trailers.get(adapterPosition);
            mClickHandler.onClick(url);
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

//        int layoutId;
//        switch (viewType) {
//
//            case VIEW_TYPE_TODAY: {
//                layoutId = R.layout.list_item_forecast_today;
//                break;
//            }
//
//            case VIEW_TYPE_FUTURE_DAY: {
//                layoutId = R.layout.forecast_list_item;
//                break;
//            }
//
//            default:
//                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
//        }
//
//        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
//
//        view.setFocusable(true);
//
//        return new ForecastAdapterViewHolder(view);

    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        forecastAdapterViewHolder.trailer.setText("Trailer "+Integer.toString(++position));
    }


    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }


    public void setTrailerData(ArrayList<String> data) {
        if(data==null) {return;}
        trailers = data;
        notifyDataSetChanged();
    }


}