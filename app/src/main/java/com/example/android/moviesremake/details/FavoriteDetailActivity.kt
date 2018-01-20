package com.example.android.moviesremake.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.example.android.moviesremake.R
import com.example.android.moviesremake.realm.MovieRealm
import com.example.android.moviesremake.utils.HelperRxClass
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail_relative.*


class FavoriteDetailActivity : AppCompatActivity(), DetailAdapter.ForecastAdapterOnClickHandler {

    private val mAdapter: DetailAdapter? = null
    private var movieId: Int = 0
    private lateinit var realm: Realm
    internal lateinit var movie: MovieRealm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_relative)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        favorite_button.visibility = View.GONE


        movieId = intent.getIntExtra("id", 0) // ID of movie
        if (movieId == 0) throw NullPointerException("ID for DetailActivity cannot be null")

        realm = Realm.getDefaultInstance()
        movie = realm.where(MovieRealm::class.java).equalTo("movieID", movieId).findFirst()

        // set data
        Picasso.with(this)
                .load(movie.image)
                .placeholder(R.drawable.no_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.no_image)
                .into(imageView1)

        overview.text = movie.overview
        release.text = movie.release
        vote.text = movie.vote!! + "/10 "


        val hp = HelperRxClass(movieId, this, mAdapter, reviews, trailers, this)
        hp.setReviews()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
        // Respond to the action bar's Back button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onClick(trailer: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/watch?v=" + trailer!!)

        // Always use string resources for UI text. This says something like "Share this photo with"
        val title = resources.getText(R.string.chooser_title) as String
        // Create and start the chooser

        val chooser = Intent.createChooser(intent, title)

        try {
            this.startActivity(chooser)
        } catch (ex: ActivityNotFoundException) {
            intent.`package` = "com.android.chrome"
            this.startActivity(chooser)
        }

    }


}
