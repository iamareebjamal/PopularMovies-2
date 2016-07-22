package areeb.udacity.popularmovies.api;


import areeb.udacity.popularmovies.BuildConfig;
import areeb.udacity.popularmovies.model.Movies;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final MovieAPI movieAPI = retrofit.create(MovieAPI.class);

    public static Call<Movies> getMoviesCall(Sort sortType) {
        return movieAPI.getMovies(sortType, BuildConfig.API_KEY);
    }

}
