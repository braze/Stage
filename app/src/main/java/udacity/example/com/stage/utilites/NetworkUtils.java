package udacity.example.com.stage.utilites;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import udacity.example.com.stage.BuildConfig;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String SINGLE_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";

    public final static String POPULAR = "movie/popular";

    public final static String TOP_RATED = "movie/top_rated";

    public final static String DISCOVER = "discover/movie";


    //picture size for poster query
    private static final String SIZE = "w500";

    private static final String TRAILER = "videos";

    private static final String REVIEW = "reviews";

    private static final String API_KEY = BuildConfig.API_KEY;

    private final static String API_KEY_PARAM = "api_key";

    /**
     * Build the URL based on sort
     *
     * @param sortBy The sort type that will be queried for.
     * @return The URL to use to query the movie DB server.
     */
    public static URL buildUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        Log.d(TAG, "LOOK OUT!!! JUST Url " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Build the URL for poster
     *
     * @param posterPath The path for poster.
     * @return The String to use for query.
     */
    public static String buildPosterPath (String posterPath) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(SIZE)
                .appendEncodedPath(posterPath)
                .build();
        Log.d(TAG, "LOOK OUT!!! Poster Path " + builtUri);

        return builtUri.toString();

    }

    /**
     * Build the URL for Trailers request
     *
     * @param id The movie id.
     * @return The String to use for query.
     */
    public static URL buildTrailerUrl (String id) {
        Uri builtUri = Uri.parse(SINGLE_MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(TRAILER)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        Log.d(TAG, "LOOK OUT!!! Trailer Url" + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
        }

    /**
     * Build the URL for reviews request
     *
     * @param id The movie id.
     * @return The String to use for query.
     */
    public static URL buildReviewsUrl (String id) {
        Uri builtUri = Uri.parse(SINGLE_MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEW)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        Log.d(TAG, "LOOK OUT!!! Reviews Url " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Build the base information URL
     *
     * @param id The movie id.
     * @return The String to use for query.
     */
    public static URL buildBaseInformationUrl (String id) {
        Uri builtUri = Uri.parse(SINGLE_MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        Log.d(TAG, "LOOK OUT!!! Base Information Url " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
