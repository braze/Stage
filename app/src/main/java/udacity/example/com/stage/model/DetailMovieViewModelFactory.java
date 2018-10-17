package udacity.example.com.stage.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import udacity.example.com.stage.database.AppDatabase;

public class DetailMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String mMovieId;

    public DetailMovieViewModelFactory(AppDatabase database, String movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mDb, mMovieId);
    }
}
