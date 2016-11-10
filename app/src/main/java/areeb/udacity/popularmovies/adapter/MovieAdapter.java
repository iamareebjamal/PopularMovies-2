package areeb.udacity.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import areeb.udacity.popularmovies.DetailActivity;
import areeb.udacity.popularmovies.MainActivity;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.fragment.MoviesFragment;
import areeb.udacity.popularmovies.model.Movie;
import areeb.udacity.popularmovies.model.Movies;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private static final String TAG = "Movie Adapter";

    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public MovieAdapter(Context context, Movies movies) {
        this.context = context;
        this.movies = movies.getMovies();
    }

    public void changeDataSet(Movies movies) {
        this.movies = movies.getMovies();
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, int position) {
        final Movie movie = movies.get(position);

        if (holder.movieTitle.getText().equals(context.getString(R.string.movie_title))) {
            holder.rootView.setVisibility(View.GONE);
        }

        holder.movieTitle.setText(movie.getTitle());

        holder.moviePanel.setBackgroundColor(context.getResources().getColor(R.color.basic_light));
        holder.movieTitle.setTextColor(context.getResources().getColor(R.color.basic_dark));

        holder.rootView.setPreventCornerOverlap(false);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isDualPane()){
                    MoviesFragment.movieSelector.onSelect(movie);
                } else {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(Movie.TAG, movie);
                    context.startActivity(intent);
                }
            }
        });

        Picasso.with(context).load(movie.getPoster()).into(holder.posterHolder, new Callback() {

            @Override
            public void onSuccess() {
                holder.rootView.setVisibility(View.VISIBLE);
                Utils.from(context).colorize(holder);
            }

            @Override
            public void onError() {
                Log.d(TAG, context.getString(R.string.image_load_error));
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootcard) public CardView rootView;
        @BindView(R.id.poster_holder) public ImageView posterHolder;
        @BindView(R.id.movie_title) public TextView movieTitle;
        @BindView(R.id.movie_panel) public RelativeLayout moviePanel;
        @BindView(R.id.like) public ImageView favourite;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
