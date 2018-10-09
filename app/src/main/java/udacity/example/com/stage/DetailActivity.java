package udacity.example.com.stage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailActivity extends AppCompatActivity implements OnDetailTaskCompleted{

    private static final String TAG = DetailActivity.class.getSimpleName();


    public static final String EXTRA_OBJECT = "extra_position";

    @BindView(R.id.release_date_tv)
    TextView releaseDate;

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

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = intent.getExtras().getParcelable(EXTRA_OBJECT);
        String fullPosterPath = NetworkUtils.buildPosterPath(movie.getPosterPath());
        String movieId = movie.getMovieId();

        makeMovieDetailQuery(movieId);

        populateUI(movie);
        Picasso.with(this).load(fullPosterPath).error(R.drawable.ic_no_poster).into(poster);
        setTitle(movie.getTitle());
    }

    private void makeMovieDetailQuery(String movieId) {

        //check for internet connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
//            URL searchUrl = NetworkUtils.buildUrl(query);
            // TODO: 10/8/18 make progress bar
//            mProgressBar.setVisibility(View.VISIBLE);
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
    public void OnDetailTaskCompleted(List<MovieDetail> list) {
        // TODO: 10/8/18 get data from asyncTask
//        mAdapter.setMoviesList(list);
//        mProgressBar.setVisibility(View.GONE);
    }
}
