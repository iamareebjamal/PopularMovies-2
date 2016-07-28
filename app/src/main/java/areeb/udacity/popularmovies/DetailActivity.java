package areeb.udacity.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import areeb.udacity.popularmovies.adapter.TrailerAdapter;
import areeb.udacity.popularmovies.model.*;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.plot) TextView plot;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        //Handle received data
        if (getIntent().hasExtra(Movie.TAG)) {
            movie = getIntent().getParcelableExtra(Movie.TAG);

            collapsingToolbar.setTitle(movie.getTitle());
            collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.color_primary));
            collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.color_primary));

            plot.setText(movie.getPlot());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.from(DetailActivity.this).shareMovie(movie);
                }
            });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.share_movie), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            loadImages();
            loadTrailers();
        }
    }


    @BindView(R.id.plotHolder) TextView plotHolder;
    @BindView(R.id.infoPanel) RelativeLayout infoPanel;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.dateIcon) ImageView dateIcon;
    @BindView(R.id.rate) TextView rate;
    @BindView(R.id.rateIcon) ImageView rateIcon;
    @BindView(R.id.genre) TextView genre;
    @BindView(R.id.genreIcon) ImageView genreIcon;

    @BindView(R.id.trailerHolder) TextView trailerHolder;

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

    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.backdrop) ImageView backdrop;
    private void loadImages() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            backdrop.setBackgroundResource(R.drawable.vector_movies);

        Picasso.with(this).load(movie.getBackdrop()).into(backdrop, new com.squareup.picasso.Callback() {
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


        Picasso.with(this).load(movie.getPoster()).into(poster, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                setDetails(poster);
            }

            @Override
            public void onError() {
            }
        });
    }

    @BindView(R.id.trailers)
    RecyclerView trailersRv;
    private void loadTrailers(){
        final TrailerAdapter trailerAdapter = new TrailerAdapter(this, new Trailers());
        trailersRv.setAdapter(trailerAdapter);

        trailersRv.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
