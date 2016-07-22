package areeb.udacity.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import areeb.udacity.popularmovies.model.Movie;
import areeb.udacity.popularmovies.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        //Handle received data
        if (getIntent().hasExtra(Movie.TAG)) {
            movie = getIntent().getParcelableExtra(Movie.TAG);

            collapsingToolbar = ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar));
            collapsingToolbar.setTitle(movie.getTitle());
            collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.color_primary));
            collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.color_primary));

            TextView plot = (TextView) findViewById(R.id.plot);
            plot.setText(movie.getPlot());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        }
    }

    private void setDetails(ImageView imageView) {
        if (imageView == null)
            return;

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();

                int color = getResources().getColor(R.color.color_primary);
                int titleColor = getResources().getColor(R.color.basic_dark_transparent);

                if (swatch != null) {
                    color = swatch.getRgb();
                    titleColor = swatch.getTitleTextColor();
                }

                collapsingToolbar.setBackgroundColor(color);
                collapsingToolbar.setStatusBarScrimColor(Utils.getDarkColor(color));
                collapsingToolbar.setContentScrimColor(color);

                TextView plotHolder = (TextView) findViewById(R.id.plotHolder);
                plotHolder.setBackgroundColor(color);
                plotHolder.setTextColor(titleColor);

                RelativeLayout infoPanel = (RelativeLayout) findViewById(R.id.infoPanel);
                infoPanel.setBackgroundColor(color);

                TextView date = (TextView) findViewById(R.id.date);
                date.setText(movie.getReleaseDate());
                date.setTextColor(titleColor);

                ImageView dateIcon = (ImageView) findViewById(R.id.dateIcon);
                Utils.setTint(dateIcon, titleColor);

                TextView rate = (TextView) findViewById(R.id.rate);
                rate.setText(String.valueOf(movie.getRating()) + "/10");
                rate.setTextColor(titleColor);

                ImageView rateIcon = (ImageView) findViewById(R.id.rateIcon);
                Utils.setTint(rateIcon, titleColor);

                TextView genre = (TextView) findViewById(R.id.genre);
                genre.setText(movie.getGenres());
                genre.setTextColor(titleColor);

                ImageView genreIcon = (ImageView) findViewById(R.id.genreIcon);
                Utils.setTint(genreIcon, titleColor);

            }
        });

    }

    private void loadImages() {
        final ImageView poster = (ImageView) findViewById(R.id.poster);

        final ImageView backdrop = (ImageView) findViewById(R.id.backdrop);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            backdrop.setBackgroundResource(R.drawable.vector_movies);

        Picasso.with(this).load(movie.getBackdrop()).into(backdrop, new Callback() {
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


        Picasso.with(this).load(movie.getPoster()).into(poster, new Callback() {
            @Override
            public void onSuccess() {
                setDetails(poster);
            }

            @Override
            public void onError() {
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
