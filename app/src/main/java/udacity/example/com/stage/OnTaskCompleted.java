package udacity.example.com.stage;

import java.util.List;

import udacity.example.com.stage.model.Movie;

public interface OnTaskCompleted {
    void onTaskCompleted(List<Movie> list);
}
