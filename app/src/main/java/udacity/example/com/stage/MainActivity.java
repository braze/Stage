package udacity.example.com.stage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.utilites.JsonUtils;
import udacity.example.com.stage.utilites.NetworkUtils;

import static udacity.example.com.stage.DetailActivity.EXTRA_OBJECT;
import static udacity.example.com.stage.utilites.NetworkUtils.DISCOVER;
import static udacity.example.com.stage.utilites.NetworkUtils.POPULAR;
import static udacity.example.com.stage.utilites.NetworkUtils.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

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
        intent.putExtra(EXTRA_OBJECT, (Serializable) movie);
        startActivity(intent);
    }

    private void makeMovieQuery(String query) {
        URL searchUrl = NetworkUtils.buildUrl(query);
        new MovieQueryTask().execute(searchUrl);
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

    public class MovieQueryTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL searchUrl = urls[0];

            String movieSearchResult = null;
            //create list of Movie objects
            ArrayList<Movie> list = new ArrayList<>();
            try {
                //build URL and populate list with Movie objects
                movieSearchResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                list = JsonUtils.populateJsonList(movieSearchResult);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Movie> list) {
            super.onPostExecute(list);
            //set the same list to adapter
            mAdapter.setMoviesList(list);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
