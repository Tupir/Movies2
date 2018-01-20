package com.example.android.moviesremake.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.android.moviesremake.R
import com.example.android.moviesremake.realm.MovieRealm
import com.example.android.moviesremake.retrofit.model.MovieRetrofit
import com.example.android.moviesremake.utils.HelperRxClass
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail_relative.*
import java.util.*

class DetailActivity : AppCompatActivity(), DetailAdapter.ForecastAdapterOnClickHandler {

    private lateinit var movie: MovieRetrofit
    private val mAdapter: DetailAdapter? = null
    private var realm: Realm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_relative)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val intentThatStartedThisActivity = intent
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movies")) {
                movie = intentThatStartedThisActivity.getParcelableExtra("movies")
                title = movie.title
                Picasso.with(this).load(movie.getImage()).into(imageView1)
                release.text = movie.release
                vote.text = movie.vote + "/10"
                overview.text = movie.overview
            }
        }

        realm = Realm.getDefaultInstance()

        //http://api.themoviedb.org/3/movie/346364/reviews?api_key=c88f3eabe09958ae472c9cd7e20b38aa


        val hp = HelperRxClass(movie.id, this, mAdapter, reviews, trailers, this)
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

    // maybe code an delete from database after clicking on it again?
    fun insertMovieToDatabase(view: View) {

        realm!!.executeTransactionAsync { realm ->
            var task: MovieRealm? = realm.where(MovieRealm::class.java).equalTo("image", movie.getImage()).findFirst()
            if (task == null) {
                task = realm.createObject(MovieRealm::class.java, UUID.randomUUID().toString())
                task.image = movie.getImage()
                task.movieID = movie.id
                task.overview = movie.overview
                task.release = movie.release
                task.vote = movie.vote
            } else {
                runOnUiThread { Toast.makeText(applicationContext, "Already in favorites!", Toast.LENGTH_SHORT).show() }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        realm!!.close()
    }


    override fun onClick(trailer: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/watch?v=" + trailer)

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
