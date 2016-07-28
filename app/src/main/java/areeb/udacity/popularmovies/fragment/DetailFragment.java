package areeb.udacity.popularmovies.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import areeb.udacity.popularmovies.DetailActivity;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.adapter.ReviewAdapter;
import areeb.udacity.popularmovies.adapter.TrailerAdapter;
import areeb.udacity.popularmovies.model.Movie;
import areeb.udacity.popularmovies.model.Reviews;
import areeb.udacity.popularmovies.model.Trailers;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private Movie movie;

    public DetailFragment(){

    }

    public static DetailFragment newInstance(Movie movie){
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.TAG, movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detail, container, false);
        ButterKnife.bind(this, rootView);

        // If instantiated by activity
        movie = getArguments().getParcelable(Movie.TAG);
        if(movie!=null){
            setupViews();
            ((DetailActivity)getActivity()).setBackButton(toolbar);
        }

        return rootView;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
        setupViews();
    }

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.plot)
    TextView plot;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private void setupViews(){
        //Handle received data
        if (movie!=null) {

            collapsingToolbar.setTitle(movie.getTitle());
            collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.color_primary));
            collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.color_primary));

            plot.setText(movie.getPlot());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.from(getActivity()).shareMovie(movie);
                }
            });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getContext(), getResources().getString(R.string.share_movie), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            loadImages();
            loadTrailers();
            loadReviews();
        }
    }

    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.backdrop) ImageView backdrop;
    private void loadImages() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            backdrop.setBackgroundResource(R.drawable.vector_movies);

        Picasso.with(getContext()).load(movie.getBackdrop()).into(backdrop, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Reset the margin
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(backdrop.getLayoutParams());
                marginParams.setMargins(0, 0, 0, 0);
                CollapsingToolbarLayout.LayoutParams layoutParams = new CollapsingToolbarLayout.LayoutParams(marginParams);
                backdrop.setLayoutParams(layoutParams);
            }

            @Override
            public void onError() {
            }
        });


        Picasso.with(getContext()).load(movie.getPoster()).into(poster, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                setDetails(poster);
            }

            @Override
            public void onError() {
            }
        });
    }

    @BindView(R.id.reviews)
    RecyclerView reviewsRv;
    private void loadReviews(){
        final ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), new Reviews());
        reviewsRv.setAdapter(reviewAdapter);

        reviewsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        final Call<Reviews> reviewsCall = movie.getReviewsCall();
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                reviewAdapter.changeReviews(reviews);

                if(reviews.getReviews().size()==0){
                    reviewsRv.setVisibility(View.GONE);
                    reviewHolder.setText(R.string.no_review);
                } else {
                    reviewsRv.setVisibility(View.VISIBLE);
                    reviewHolder.setText(R.string.review);
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    @BindView(R.id.trailers)
    RecyclerView trailersRv;
    private void loadTrailers(){
        final TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(), new Trailers());
        trailersRv.setAdapter(trailerAdapter);

        trailersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        Call<Trailers> trailersCall = movie.getTrailersCall();
        trailersCall.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                Trailers trailers = response.body();
                trailerAdapter.changeTrailers(trailers);

                if(trailers.getTrailers().size()==0){
                    trailersRv.setVisibility(View.GONE);
                    trailerHolder.setText(R.string.no_trailer);
                } else {
                    trailersRv.setVisibility(View.VISIBLE);
                    trailerHolder.setText(R.string.trailer);
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {

            }
        });
    }

    @BindView(R.id.plotHolder) TextView plotHolder;
    @BindView(R.id.infoPanel)
    RelativeLayout infoPanel;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.dateIcon) ImageView dateIcon;
    @BindView(R.id.rate) TextView rate;
    @BindView(R.id.rateIcon) ImageView rateIcon;
    @BindView(R.id.genre) TextView genre;
    @BindView(R.id.genreIcon) ImageView genreIcon;

    @BindView(R.id.trailerHolder) TextView trailerHolder;
    @BindView(R.id.reviewHolder) TextView reviewHolder;

    @BindColor(R.color.color_primary) int color;
    @BindColor(R.color.basic_dark_transparent) int titleColor;
    private void setDetails(ImageView imageView) {
        if (imageView == null)
            return;

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();

                if (swatch != null) {
                    color = swatch.getRgb();
                    titleColor = swatch.getTitleTextColor();
                }

                collapsingToolbar.setBackgroundColor(color);
                collapsingToolbar.setStatusBarScrimColor(Utils.getDarkColor(color));
                collapsingToolbar.setContentScrimColor(color);

                plotHolder.setBackgroundColor(color);
                plotHolder.setTextColor(titleColor);

                trailerHolder.setBackgroundColor(color);
                trailerHolder.setTextColor(titleColor);

                reviewHolder.setBackgroundColor(color);
                reviewHolder.setTextColor(titleColor);

                infoPanel.setBackgroundColor(color);

                date.setText(movie.getReleaseDate());
                date.setTextColor(titleColor);

                Utils.setTint(dateIcon, titleColor);

                rate.setText(String.valueOf(movie.getRating()) + "/10");
                rate.setTextColor(titleColor);

                Utils.setTint(rateIcon, titleColor);

                genre.setText(movie.getGenres());
                genre.setTextColor(titleColor);

                Utils.setTint(genreIcon, titleColor);

            }
        });

    }
}
