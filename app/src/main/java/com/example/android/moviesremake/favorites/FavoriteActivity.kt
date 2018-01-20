package com.example.android.moviesremake.favorites

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.android.moviesremake.R
import com.example.android.moviesremake.details.FavoriteDetailActivity
import com.example.android.moviesremake.realm.MovieRealm
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteActivity : AppCompatActivity(), FavoriteRealmAdapter.ForecastAdapterOnClickHandler {

    private var realm: Realm? = null
    internal lateinit var adapter: FavoriteRealmAdapter
    internal var tasks: RealmResults<MovieRealm>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "My favorites"


        realm = Realm.getDefaultInstance()
        tasks = realm!!.where(MovieRealm::class.java).findAll()
        adapter = FavoriteRealmAdapter(tasks, true, this, this)


        val layoutManager = GridLayoutManager(this, 2)
        rv_numbers.layoutManager = layoutManager
        rv_numbers.setHasFixedSize(true)

        rv_numbers.adapter = adapter

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        val inflater = menuInflater
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.favorite, menu)
        /* Return true so that the menu is displayed in the Toolbar */
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val intent: Intent

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
            return true
        }

        if (id == R.id.action_refresh) {
            adapter.setMoviesData(tasks!!)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onClick(movieRealm: MovieRealm) {
        val context = this
        val intentToStartDetailActivity = Intent(context, FavoriteDetailActivity::class.java)
        val x = movieRealm.movieID
        intentToStartDetailActivity.putExtra("id", x)

        startActivity(intentToStartDetailActivity)
    }

}
