package com.example.android.moviesremake.details;

/**
 * Created by PepovPC on 7/16/2017.
 * Adapter, ktory zobrazi jednotlive views
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesremake.R;

import java.util.ArrayList;
import java.util.List;


public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DetailAdapter.class.getSimpleName();
    private ArrayList<String> trailers;
    private Context context;
    private final ForecastAdapterOnClickHandler mClickHandler;
    private static final int VIEW_TRAILERS = 0;
    private static final int VIEW_REVIEWS = 1;
    List<List<String>> reviews = new ArrayList<>();


    public interface ForecastAdapterOnClickHandler {
        void onClick(String trailer);
    }

    public DetailAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;   // for Clicking
    }


    public class trailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView trailer;

        public trailerViewHolder(View itemView) {
            super(itemView);
            trailer = (TextView) itemView.findViewById(R.id.trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String url = reviews.get(adapterPosition).get(0);
            mClickHandler.onClick(url);
        }
    }

    public class reviewViewHolder extends RecyclerView.ViewHolder{

        public final TextView name;
        public final TextView text;

        public reviewViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            text = (TextView) itemView.findViewById(R.id.text);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == VIEW_TRAILERS) {
            View v = layoutInflater.inflate(R.layout.trailer_item, viewGroup, false);

            return new trailerViewHolder(v);
        } else {
            View v = layoutInflater.inflate(R.layout.review_item, viewGroup, false);

            return new reviewViewHolder(v);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch(viewType){
            case VIEW_TRAILERS:
                trailerViewHolder trailerViewHolder = (DetailAdapter.trailerViewHolder) holder;
                //trailerViewHolder.trailer.setText(trailers.get(position));
                trailerViewHolder.trailer.setText("Trailer "+Integer.toString(++position));
                break;
            case VIEW_REVIEWS:
                reviewViewHolder reviewViewHolder = (DetailAdapter.reviewViewHolder) holder;
                reviewViewHolder.name.setText(reviews.get(position).get(0)+": ");
                reviewViewHolder.text.setText(reviews.get(position).get(1));

                break;
            default:
                System.out.println("Error in onBindViewHolder method");
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(reviews.get(position).size()==1)
            return VIEW_TRAILERS;
        else
            return VIEW_REVIEWS;
    }


    @Override
    public int getItemCount() {
        if (null == reviews) return 0;
        return reviews.size();
    }


    public void setTrailerData(List<List<String>> data) {
        if(data==null) {return;}
        reviews = data;
        notifyDataSetChanged();
    }


}