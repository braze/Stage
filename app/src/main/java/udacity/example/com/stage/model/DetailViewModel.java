package udacity.example.com.stage.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import udacity.example.com.stage.database.AppDatabase;

public class DetailViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public DetailViewModel(AppDatabase database, String movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

}
