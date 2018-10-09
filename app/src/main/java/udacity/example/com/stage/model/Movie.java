package udacity.example.com.stage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String movieId;
    private String releaseDate;
    private String posterPath;
    private String voteAverage;
    private String plotSynopsis;
    private String trailerName;
    private String trailerPath;
    private String site;
    private String size;
    private String author;
    private String content;

    public Movie() {
    }

    public Movie(String title, String movieId, String releaseDate, String posterPath, String voteAverage, String plotSynopsis) {
        this.title = title;
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public Movie(String trailerName, String trailerPath, String site, String size) {
        this.trailerName = trailerName;
        this.trailerPath = trailerPath;
        this.site = site;
        this.size = size;
    }

    public Movie(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        movieId = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(movieId);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
