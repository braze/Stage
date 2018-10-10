package udacity.example.com.stage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.JsonUtils;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailMovieQueryAsyncTask extends AsyncTask<String, Void, MovieDetail> {

    private static final String TAG = DetailMovieQueryAsyncTask.class.getSimpleName();


    private OnDetailTaskCompleted detailTaskCompleted;

    public DetailMovieQueryAsyncTask (OnDetailTaskCompleted activityContext){
        this.detailTaskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: Starting!!!");
    }

    @Override
    protected MovieDetail doInBackground(String... strings) {
        String movieId = strings[0];

        Log.d(TAG, "doInBackground: Starting");

        MovieDetail detailsObject = null;
        //create list
        ArrayList<Movie> trailersList;
        ArrayList<Movie> reviewsList;
        String runtime = "";

        //build URLs for requesting data
        URL trailersUrl = NetworkUtils.buildTrailerUrl(movieId);
        URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);
        URL movieBaseInfoUrl = NetworkUtils.buildBaseInformationUrl(movieId);
        Log.d(TAG, "doInBackground: URLs has been built");

        try {
            //get JSON strings and populate list with Movie objects
            String trailersSearchResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            trailersList = JsonUtils.getTrailersList(trailersSearchResult);

            String reviewsSearchResult = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
            reviewsList = JsonUtils.getReviewsList(reviewsSearchResult);

            String movieBaseInfoSearchResult = NetworkUtils.getResponseFromHttpUrl(movieBaseInfoUrl);
            runtime = JsonUtils.getMovieBaseInfo(movieBaseInfoSearchResult);

            detailsObject = new MovieDetail(trailersList, reviewsList, runtime);
            Log.d(TAG, "doInBackground: OBJECT HAS BEEN BUILT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailsObject;
    }

    @Override
    protected void onPostExecute(MovieDetail obj) {
        super.onPostExecute(obj);
        Log.d(TAG, "onPostExecute: READY TO go home!!!1");
        detailTaskCompleted.onDetailTaskCompleted(obj);
    }
}
