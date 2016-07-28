package areeb.udacity.popularmovies.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import areeb.udacity.popularmovies.MainActivity;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.adapter.MovieAdapter;
import areeb.udacity.popularmovies.api.MovieService;
import areeb.udacity.popularmovies.api.Sort;
import areeb.udacity.popularmovies.model.Movie;
import areeb.udacity.popularmovies.model.Movies;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class MoviesFragment extends Fragment implements Callback<Movies> {
    private static final String TAG = "Movies Fragment";

    private static final String SORT_TYPE = "sort";
    private static final String MOVIE_KEY = "movies";

    private Sort sortType = Sort.POPULAR;

    private View rootView;
    private MovieAdapter movieAdapter;
    private Movies movies;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(Sort sortType) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(SORT_TYPE, sortType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(SORT_TYPE)) {
            sortType = (Sort) getArguments().getSerializable(SORT_TYPE);
        }
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);

        setupViews();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)) {
            List<Movie> list = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            movies = new Movies(list);
            movieAdapter.changeDataSet(movies);
            rootView.findViewById(R.id.hidden).setVisibility(View.GONE);
        } else {
            Call<Movies> call = MovieService.getMoviesCall(sortType);
            call.enqueue(this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_KEY, (ArrayList) movies.getMovies());
    }

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.movie_list) RecyclerView recyclerView;
    private void setupViews() {
        int columns = 2;

        movies = new Movies();
        movieAdapter = new MovieAdapter(getActivity(), movies);
        recyclerView.setAdapter(movieAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columns));
        Utils.setScrollBehavior(fab, recyclerView);

        setRandomLoadingMessage();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle
                if (sortType.equals(Sort.POPULAR))
                    refresh(Sort.TOP_RATED);
                else
                    refresh(Sort.POPULAR);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), getContext().getString(R.string.toggle_movies), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setRandomLoadingMessage() {
        TextView loading = (TextView) rootView.findViewById(R.id.loadingMessage);
        loading.setText(Utils.from(getActivity()).getNextLoadingMessage());
    }

    private void refresh(Sort sortType) {
        this.sortType = sortType;

        movies.getMovies().clear();
        movieAdapter.notifyDataSetChanged();
        Call<Movies> call = MovieService.getMoviesCall(sortType);
        call.enqueue(this);

        setRandomLoadingMessage();

        rootView.findViewById(R.id.hidden).setVisibility(View.VISIBLE);
    }


    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        if (response.isSuccessful()) {
            movies = response.body();
            movieAdapter.changeDataSet(movies);
            Snackbar.make(rootView, getActivity().getString(R.string.movies_load_success), Snackbar.LENGTH_SHORT).show();

            rootView.findViewById(R.id.hidden).setVisibility(View.GONE);

            ((MainActivity) getActivity()).setTitle(sortType.toString() + " Movies");
        }
    }

    @Override
    public void onFailure(final Call<Movies> call, Throwable t) {
        Snackbar failed = Snackbar.make(rootView, getActivity().getString(R.string.movies_load_retry), Snackbar.LENGTH_INDEFINITE);
        failed.setAction(getActivity().getString(R.string.movies_load_retry), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                refresh(sortType);
            }
        });
        failed.show();
        rootView.findViewById(R.id.hidden).setVisibility(View.VISIBLE);
        Log.d(TAG, t.getLocalizedMessage());
    }
}
