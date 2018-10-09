package udacity.example.com.stage;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.model.MovieDetail;
import udacity.example.com.stage.utilites.JsonUtils;
import udacity.example.com.stage.utilites.NetworkUtils;

public class DetailMovieQueryAsyncTask extends AsyncTask<String, Void, List<MovieDetail>> {

    private OnDetailTaskCompleted detailTaskCompleted;


    public DetailMovieQueryAsyncTask (OnDetailTaskCompleted activityContext){
        this.detailTaskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<MovieDetail> doInBackground(String... strings) {
        String movieId = strings[0];

        List<MovieDetail> detailsObject = new ArrayList<>();
        //create list of Movie objects
        ArrayList<Movie> trailersList = new ArrayList<>();
        ArrayList<Movie> reviewsList = new ArrayList<>();
        String runtime = "";

        //build URLs for requesting data
        URL trailersUrl = NetworkUtils.buildTrailerUrl(movieId);
        URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);
        URL movieBaseInfoUrl = NetworkUtils.buildBaseInformationUrl(movieId);

        try {
            //build URL and populate list with Movie objects
            String trailersSearchResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            trailersList = JsonUtils.getTrailersList(trailersSearchResult);

            String reviewsSearchResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            reviewsList = JsonUtils.getReviewsList(reviewsSearchResult);

            String movieBaseInfoSearchResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            runtime = JsonUtils.getMovieBaseInfo(movieBaseInfoSearchResult);

            detailsObject.add(new MovieDetail(trailersList, reviewsList, runtime));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailsObject;
    }

    @Override
    protected void onPostExecute(List<MovieDetail> list) {
        super.onPostExecute(list);
        detailTaskCompleted.OnDetailTaskCompleted(list);
    }
}
