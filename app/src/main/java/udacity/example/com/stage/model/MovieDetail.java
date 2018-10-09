package udacity.example.com.stage.model;

import java.util.ArrayList;

public class MovieDetail {
    private ArrayList<Movie> trailersList;
    private ArrayList<Movie> reviewsList;
    private String runtime;

    public MovieDetail(ArrayList<Movie> trailersList, ArrayList<Movie> reviewsList, String runtime) {
        this.trailersList = trailersList;
        this.reviewsList = reviewsList;
        this.runtime = runtime;
    }

    public ArrayList<Movie> getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(ArrayList<Movie> trailersList) {
        this.trailersList = trailersList;
    }

    public ArrayList<Movie> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(ArrayList<Movie> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
