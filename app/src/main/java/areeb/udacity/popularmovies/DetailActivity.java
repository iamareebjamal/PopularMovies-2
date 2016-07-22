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
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
