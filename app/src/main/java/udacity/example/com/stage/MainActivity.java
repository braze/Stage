package udacity.example.com.stage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.ArrayList;
import java.util.List;

import udacity.example.com.stage.model.MainViewModel;
import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.utilites.NetworkUtils;

import static udacity.example.com.stage.DetailActivity.EXTRA_OBJECT;
import static udacity.example.com.stage.utilites.NetworkUtils.DISCOVER;
import static udacity.example.com.stage.utilites.NetworkUtils.POPULAR;
import static udacity.example.com.stage.utilites.NetworkUtils.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, OnTaskCompleted {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT = "MainActivity.recycler.layout";
    private static final String QUERY_PARAM = "query_param";
    private static final String MOVIES_LIST_STATE = "movies_list";
    public final static String FAVORITE = "favorite";

    private String mQuery = DISCOVER;
    private MovieAdapter mAdapter;
    private RecyclerView mFrontPageList;
    private ProgressBar mProgressBar;
    private GridLayoutManager mLayoutManager;
    private Parcelable mSavedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mFrontPageList = (RecyclerView) findViewById(R.id.rv_numbers);

        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mFrontPageList.setLayoutManager(mLayoutManager);
        mFrontPageList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);


        if(savedInstanceState != null) {
            mQuery = savedInstanceState.getString(QUERY_PARAM);
            Log.d(TAG, "onCreate: " + mQuery);

            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mFrontPageList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            if (mQuery.equals(FAVORITE)){
                MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
                viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        mAdapter.setMoviesList(movies);
                    }
                });
            }else {
                List<Movie> list = savedInstanceState.getParcelableArrayList(MOVIES_LIST_STATE);
                mAdapter.setMoviesList(list);
            }
            mFrontPageList.setAdapter(mAdapter);
        } else {
            mAdapter.setMoviesList(mAdapter.getMoviesList());
            mFrontPageList.setAdapter(mAdapter);
            makeMovieQuery(mQuery);
        }
        setLayoutTitle();
    }

    /**
     * Movie adapter click handler
     *
     * @param position position of recyclerView
     */
    @Override
    public void onClick(int position) {
        Movie movie = mAdapter.getMoviesList().get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_OBJECT, movie);
        startActivity(intent);
    }

    /**
     * Make query depending on query
     *
     * @param query The sort type that will be queried for.
     */
    private void makeMovieQuery(String query) {
        //check for internet connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

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
            mQuery = POPULAR;
            makeMovieQuery(mQuery);
            setLayoutTitle();
            return true;
        }
        if (id == R.id.action_sort_by_rating) {
            mQuery = TOP_RATED;
            makeMovieQuery(mQuery);
            setLayoutTitle();
            return true;
        }
        if (id == R.id.action_favorite) {
            mQuery = FAVORITE;
            //Set list of movies from LiveData in ViewModel
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    mAdapter.setMoviesList(movies);
                }
            });
            setLayoutTitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * method is invoke after MovieQueryAsyncTask is completed
     *
     * @param list of Movie obj with main info
     */
    @Override
    public void onTaskCompleted(List<Movie> list) {
        mAdapter.setMoviesList(list);
        mFrontPageList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUERY_PARAM, mQuery);
        Log.d(TAG, "onSaveInstanceState: query" + mQuery);
        if (!(mQuery.equals(FAVORITE))) {
            outState.putParcelableArrayList(MOVIES_LIST_STATE, (ArrayList<? extends Parcelable>) mAdapter.getMoviesList());
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mFrontPageList.getLayoutManager().onSaveInstanceState());
        }
    }

    private void setLayoutTitle() {
        switch (mQuery) {
            case POPULAR:
                setTitle(getString(R.string.Popular_title));
                break;
            case TOP_RATED:
                setTitle(getString(R.string.top_rated_title));
                break;
            case DISCOVER:
                setTitle(getString(R.string.Popular_title));
                break;
            case FAVORITE:
                setTitle(getString(R.string.favorite_title));
                break;
            default:setTitle(getString(R.string.favorite_title));
        }
    }

}
