package areeb.udacity.popularmovies.api;


import areeb.udacity.popularmovies.BuildConfig;
import areeb.udacity.popularmovies.model.Movies;
import areeb.udacity.popularmovies.model.RealmInt;
import areeb.udacity.popularmovies.model.Reviews;
import areeb.udacity.popularmovies.model.Trailers;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.Type;


public class MovieService {

    private static final Type token = new TypeToken<RealmList<RealmInt>>(){}.getType();
    private static final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmInt>>() {

                @Override
                public void write(JsonWriter out, RealmList<RealmInt> value) throws IOException {
                    // Ignore
                }

                @Override
                public RealmList<RealmInt> read(JsonReader in) throws IOException {
                    RealmList<RealmInt> list = new RealmList<RealmInt>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(new RealmInt(in.nextInt()));
                    }
                    in.endArray();
                    return list;
                }
            })
            .create();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private static final MovieAPI movieAPI = retrofit.create(MovieAPI.class);

    public static Call<Movies> getMoviesCall(Sort sortType) {
        return movieAPI.getMovies(sortType);
    }

    public static Call<Reviews> getReviewsCall(int id) {
        return movieAPI.getReviews(id);
    }

    public static Call<Trailers> getTrailersCall(int id) {
        return movieAPI.getTrailers(id);
    }

}
