package udacity.example.com.stage.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import udacity.example.com.stage.model.Movie;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY movieId")
    LiveData<List<Movie>> loadAllMovies();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    LiveData<Movie> loadMovieById(String movieId);
//    Movie loadMovieById(String movieId);
}
