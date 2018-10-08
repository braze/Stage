package udacity.example.com.stage.utilites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import udacity.example.com.stage.model.Movie;

public class JsonUtils {

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
}
