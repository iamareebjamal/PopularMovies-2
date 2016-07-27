package areeb.udacity.popularmovies.api;

import areeb.udacity.popularmovies.BuildConfig;
import areeb.udacity.popularmovies.model.Movies;
import areeb.udacity.popularmovies.model.Reviews;
import areeb.udacity.popularmovies.model.Trailers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {
    String KEY_QUERY = "?api_key=" + BuildConfig.API_KEY;

    @GET("{sort}" + KEY_QUERY)
    Call<Movies> getMovies(@Path("sort") Sort sort);

    @GET("{id}/reviews" + KEY_QUERY)
    Call<Reviews> getReviews(@Path("id") int id);

    @GET("{id}/videos" + KEY_QUERY)
    Call<Trailers> getTrailers(@Path("id") int id);
}
