package udacity.example.com.stage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailActivity extends AppCompatActivity implements OnDetailTaskCompleted, MovieAdapterOnClickHandler{

    private static final String TAG = DetailActivity.class.getSimpleName();

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mTrailersList;
    private RecyclerView mReviewList;
    private Movie mMovie;
    private ProgressBar mTrailerProgressBar;
    private ProgressBar mReviewProgressBar;


    public static final String EXTRA_OBJECT = "extra_position";

    @BindView(R.id.release_date_tv)
    TextView releaseDate;

    @BindView(R.id.runtime_tv)
    TextView runtime;

    @BindView(R.id.runtime_label)
    TextView runtimeLabel;

    @BindView(R.id.average_vote_tv)
    TextView voteAverage;

    @BindView(R.id.plot_synopsis_tv)
    TextView plotSynopsis;

    @BindView(R.id.poster_iv)
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // bind the view using butterknife
        ButterKnife.bind(this);

        mTrailerProgressBar = findViewById(R.id.pb_trailer_loading_indicator);
        mReviewProgressBar = findViewById(R.id.pb_review_loading_indicator);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mMovie = intent.getExtras().getParcelable(EXTRA_OBJECT);
        String fullPosterPath = NetworkUtils.buildPosterPath(mMovie.getPosterPath());
        String movieId = mMovie.getMovieId();

        //query for details
        makeMovieDetailQuery(movieId);

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

        mReviewAdapter = new ReviewAdapter(this);
        mReviewAdapter.setReviewsList(mReviewAdapter.getReviewsList());
        mReviewList.setAdapter(mReviewAdapter);

        populateUI(mMovie);
        Picasso.with(this).load(fullPosterPath).error(R.drawable.ic_no_poster).into(poster);
        setTitle(mMovie.getTitle());
    }

    private void makeMovieDetailQuery(String movieId) {

        //check for internet connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // TODO: 10/8/18 make progress bar
            mTrailerProgressBar.setVisibility(View.VISIBLE);
            mReviewProgressBar.setVisibility(View.VISIBLE);
            new DetailMovieQueryAsyncTask(DetailActivity.this).execute(movieId);
        }
    }

    private void populateUI(Movie mMovie) {
        releaseDate.setText(mMovie.getReleaseDate());
        voteAverage.setText(mMovie.getVoteAverage());
        plotSynopsis.setText(mMovie.getPlotSynopsis());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.err_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailTaskCompleted(MovieDetail obj) {
        // TODO: 10/8/18 get data from asyncTask
        if (obj != null) {
            mTrailerAdapter.setTrailersList(obj.getTrailersList());
            mReviewAdapter.setReviewsList(obj.getReviewsList());
            runtimeLabel.setVisibility(View.VISIBLE);
            runtime.setVisibility(View.VISIBLE);
            String duration = obj.getRuntime() + " " + "min";
            runtime.setText(duration);
        mTrailerProgressBar.setVisibility(View.GONE);
        mReviewProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(int movieId) {

    }
}
