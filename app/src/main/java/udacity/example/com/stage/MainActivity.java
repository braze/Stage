package udacity.example.com.stage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.utilites.NetworkUtils;

import static udacity.example.com.stage.DetailActivity.EXTRA_OBJECT;
import static udacity.example.com.stage.utilites.NetworkUtils.DISCOVER;
import static udacity.example.com.stage.utilites.NetworkUtils.POPULAR;
import static udacity.example.com.stage.utilites.NetworkUtils.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, OnTaskCompleted {

    private MovieAdapter mAdapter;
    private RecyclerView mNumbersList;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);

        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        mAdapter.setMoviesList(mAdapter.getMoviesList());
        mNumbersList.setAdapter(mAdapter);

        makeMovieQuery(DISCOVER);
    }

    @Override
    public void onClick(int pos) {
        Movie movie = mAdapter.getMoviesList().get(pos);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_OBJECT, movie);
        startActivity(intent);
    }

    private void makeMovieQuery(String query) {

        //check for internet connection
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            URL searchUrl = NetworkUtils.buildUrl(query);
            mProgressBar.setVisibility(View.VISIBLE);
            new MovieQueryAsyncTask(MainActivity.this).execute(searchUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popular) {
            makeMovieQuery(POPULAR);
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            makeMovieQuery(TOP_RATED);
        }

        return super.onOptionsItemSelected(item);
    }

    //this method invokes after AsyncTask is completed
    @Override
    public void onTaskCompleted(List<Movie> list) {
        mAdapter.setMoviesList(list);
        mProgressBar.setVisibility(View.GONE);
    }
}
