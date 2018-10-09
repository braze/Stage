package udacity.example.com.stage.utilites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import udacity.example.com.stage.model.Movie;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();


    public static ArrayList<Movie> populateJsonList (String string) {

        ArrayList<Movie> list = new ArrayList<>();
        JSONObject obj = null;

        try {
            obj = new JSONObject(string);
            JSONArray array = obj.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {

                JSONObject objectInArray = array.getJSONObject(i);
                String id = objectInArray.getString("id");
                String title = objectInArray.getString("title");
                String releaseDate = objectInArray.getString("release_date");
                String posterPath = objectInArray.getString("poster_path");
                String voteAverage = objectInArray.getString("vote_average");
                String plotSynopsis = objectInArray.getString("overview");

                list.add(new Movie(title, id, releaseDate, posterPath, voteAverage, plotSynopsis));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Movie> getTrailersList(String string) {
        ArrayList<Movie> list = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(string);

            JSONArray array = obj.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {

                JSONObject objectInArray = array.getJSONObject(i);
                String trailerName = objectInArray.getString("name");
                String key = objectInArray.getString("key");
                String site = objectInArray.getString("site");
                String size = objectInArray.getString("size");
                String trailerPath = NetworkUtils.buildYouTubePath(key);

                list.add(new Movie(trailerName, trailerPath, site, size));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Movie> getReviewsList(String string) {
        ArrayList<Movie> list = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(string);

            JSONArray array = obj.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {

                JSONObject objectInArray = array.getJSONObject(i);
                String author = objectInArray.getString("author");
                String content = objectInArray.getString("content");
                list.add(new Movie(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getMovieBaseInfo(String string) {
        String runtime = "";
        JSONObject obj = null;
        try {
            obj = new JSONObject(string);
            runtime = obj.getString("runtime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return runtime;
    }
}
