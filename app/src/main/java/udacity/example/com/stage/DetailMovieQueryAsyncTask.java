package udacity.example.com.stage;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.JsonUtils;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailMovieQueryAsyncTask extends AsyncTask<String, Void, MovieDetail> {

    private OnDetailTaskCompleted detailTaskCompleted;

    public DetailMovieQueryAsyncTask (OnDetailTaskCompleted activityContext){
        this.detailTaskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieDetail doInBackground(String... strings) {
        String movieId = strings[0];

        MovieDetail detailsObject = null;
        //create list
        ArrayList<Movie> trailersList;
        ArrayList<Movie> reviewsList;
        String runtime = "";

        //build URLs for requesting data
        URL trailersUrl = NetworkUtils.buildTrailerUrl(movieId);
        URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);
        URL movieBaseInfoUrl = NetworkUtils.buildBaseInformationUrl(movieId);

        try {
            //get JSON strings and populate list with Movie objects
            String trailersSearchResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            trailersList = JsonUtils.getTrailersList(trailersSearchResult);

            String reviewsSearchResult = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
            reviewsList = JsonUtils.getReviewsList(reviewsSearchResult);

            String movieBaseInfoSearchResult = NetworkUtils.getResponseFromHttpUrl(movieBaseInfoUrl);
            runtime = JsonUtils.getMovieBaseInfo(movieBaseInfoSearchResult);

            //complex MovieDetail object with extra information
            detailsObject = new MovieDetail(trailersList, reviewsList, runtime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailsObject;
    }

    @Override
    protected void onPostExecute(MovieDetail obj) {
        super.onPostExecute(obj);
        detailTaskCompleted.onDetailTaskCompleted(obj);
    }
}
