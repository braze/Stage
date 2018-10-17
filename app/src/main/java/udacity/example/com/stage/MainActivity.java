package udacity.example.com.stage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;

import udacity.example.com.stage.database.AppDatabase;
import udacity.example.com.stage.model.MainViewModel;
import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.utilites.NetworkUtils;

import static udacity.example.com.stage.DetailActivity.EXTRA_OBJECT;
import static udacity.example.com.stage.utilites.NetworkUtils.DISCOVER;
import static udacity.example.com.stage.utilites.NetworkUtils.POPULAR;
import static udacity.example.com.stage.utilites.NetworkUtils.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, OnTaskCompleted {

    private static final String TAG = MainActivity.class.getSimpleName();
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

        AppDatabase.getInstance(this);

        makeMovieQuery(DISCOVER);
    }

    //Movie adapter click handler
    @Override
    public void onClick(int position) {
        Movie movie = mAdapter.getMoviesList().get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_OBJECT, movie);
        startActivity(intent);
    }

    private void makeMovieQuery(String query) {
        //check for internet connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

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
            setTitle(getString(R.string.Popular_title));
            return true;
        }
        if (id == R.id.action_sort_by_rating) {
            makeMovieQuery(TOP_RATED);
            setTitle(getString(R.string.top_rated_title));
            return true;
        }
        if (id == R.id.action_favorite) {
            //Set list of movies from LiveData in ViewModel
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.d(TAG, "Set list of movies from LiveData in ViewModel");
                    mAdapter.setMoviesList(movies);
                }
            });
            setTitle(getString(R.string.favorite_title));
            return true;
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
