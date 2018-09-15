package udacity.example.com.stage;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import udacity.example.com.stage.model.Movie;
import udacity.example.com.stage.utilites.JsonUtils;
import udacity.example.com.stage.utilites.NetworkUtils;

public class MovieQueryAsyncTask extends AsyncTask<URL, Void, List<Movie>> {

    private OnTaskCompleted taskCompleted;


    public MovieQueryAsyncTask (OnTaskCompleted activityContext){
        this.taskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        taskCompleted.onTaskCompleted(list);
        taskCompleted.onTaskCompleted(list);
    }
}
