package udacity.example.com.stage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.stage.database.AppDatabase;
import udacity.example.com.stage.model.DetailMovieViewModelFactory;
import udacity.example.com.stage.model.DetailViewModel;
import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.AppExecutor;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailActivity extends AppCompatActivity implements OnDetailTaskCompleted, MovieAdapterOnClickHandler{

    public static final String INSTANCE_MOVIE = "movie";
    public static final String EXTRA_OBJECT = "extra_position";

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mTrailersList;
    private RecyclerView mReviewList;
    private Movie mMovie;
    private String mMovieId;
    private AppDatabase mDb;
    private boolean isFavorite;

    @BindView(R.id.movie_title_tv)
    TextView title;

    @BindView(R.id.release_date_tv)
    TextView releaseDate;

    @BindView(R.id.runtime_tv)
    TextView runtime;

    @BindView(R.id.runtime_label)
    TextView runtimeLabel;

    @BindView(R.id.average_vote_tv)
    TextView voteAverage;

    @BindView(R.id.star)
    ImageView favorite;

    @BindView(R.id.plot_synopsis_tv)
    TextView plotSynopsis;

    @BindView(R.id.poster_iv)
    ImageView poster;

    @BindView(R.id.pb_trailer_loading_indicator)
    ProgressBar mTrailerProgressBar;

    @BindView(R.id.pb_review_loading_indicator)
    ProgressBar mReviewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // bind the view using butterknife
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE)) {
            mMovie = savedInstanceState.getParcelable(INSTANCE_MOVIE);
        } else {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            } else {
                mMovie = intent.getExtras().getParcelable(EXTRA_OBJECT);
            }
        }

        mMovieId = mMovie.getMovieId();

        //query for details
        makeMovieDetailQuery(mMovieId);

        //set trailer adapter
        mTrailersList = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTrailersList.getContext(),
                layoutManager.getOrientation());
        mTrailersList.addItemDecoration(dividerItemDecoration);
        mTrailersList.setLayoutManager(layoutManager);
        mTrailersList.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerAdapter.setTrailersList(mTrailerAdapter.getTrailersList());
        mTrailersList.setAdapter(mTrailerAdapter);

        //set review adapter
        mReviewList = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(layoutManager2);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mReviewList.getContext(),
                layoutManager2.getOrientation());
        mReviewList.addItemDecoration(dividerItemDecoration2);
        mReviewList.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewAdapter.setReviewsList(mReviewAdapter.getReviewsList());
        mReviewList.setAdapter(mReviewAdapter);

        //check if movie is in the database
        DetailMovieViewModelFactory factory = new DetailMovieViewModelFactory(mDb, mMovieId);
        final DetailViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    if(movie.getMovieId().equals(mMovieId)) {
                        isFavorite = true;
                        populateUI(mMovie);
                    }
                } else {
                    isFavorite = false;
                    populateUI(mMovie);
                }
            }
        });
    }

    /**
     * get Detail information, trailers, reviews, movie duration
     *
     * @param movieId movie Id
     */
    private void makeMovieDetailQuery(String movieId) {

        //check for internet connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            mTrailerProgressBar.setVisibility(View.VISIBLE);
            mReviewProgressBar.setVisibility(View.VISIBLE);
            new DetailMovieQueryAsyncTask(DetailActivity.this).execute(movieId);
        }
    }

    /**
     * populate UI
     *
     * @param movie movie obj
     */
    private void populateUI(Movie movie) {
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(movie.getVoteAverage());
        plotSynopsis.setText(movie.getPlotSynopsis());

        String fullPosterPath = NetworkUtils.buildPosterPath(movie.getPosterPath());
        Picasso.with(this).load(fullPosterPath).error(R.drawable.ic_no_poster).into(poster);

        if (isFavorite) {
            favorite.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.err_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * method starts after DetailMovieQueryAsyncTask is completed
     * set lists to their adapters and set runTime textView
     *
     * @param obj movie object with list of trailers, list of reviews
     *            and movie duration
     */
    @Override
    public void onDetailTaskCompleted(MovieDetail obj) {
        if (obj != null) {
            mTrailerAdapter.setTrailersList(obj.getTrailersList());
            mReviewAdapter.setReviewsList(obj.getReviewsList());
            runtimeLabel.setVisibility(View.VISIBLE);
            runtime.setVisibility(View.VISIBLE);
            //create duration string and set it to textView
            String duration = obj.getRuntime() + " " + "min";
            runtime.setText(duration);
        mTrailerProgressBar.setVisibility(View.GONE);
        mReviewProgressBar.setVisibility(View.GONE);
        }
    }

    //add or delete favorite movie to dataBase
    //triggered from xml file
    public void addToFavorite(View view) {
        if (!isFavorite) {
            //add this movie to db
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(mMovie);
                    favorite.setImageResource(R.drawable.ic_star_black_24dp);
                }
            });
        } else {
            //delete this movie from db
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteMovie(mMovie);
                    favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            });
        }
    }

    //Trailer adapter click handler
    @Override
    public void onClick(int adapterPosition) {
        String path = mTrailerAdapter.getTrailersList().get(adapterPosition).getTrailerPath();
        Uri file = Uri.parse(path);
        playMedia(file);
    }

    /**
     * make Intent for play trailer
     *
     * @param file Uri path for intent
     */
    private void playMedia(Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INSTANCE_MOVIE, mMovie);
        super.onSaveInstanceState(outState);
    }

}
