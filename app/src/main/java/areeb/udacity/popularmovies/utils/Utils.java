package areeb.udacity.popularmovies.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.adapter.MovieAdapter;
import areeb.udacity.popularmovies.model.Movie;

public class Utils {

    private Context context;

    public static Utils from(Context context) {
        Utils utils = new Utils();
        utils.context = context;
        return utils;
    }

    public static int getDarkColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    public static void setTint(ImageView imageView, int tintColor) {
        Drawable wrapped = DrawableCompat.wrap(imageView.getDrawable());
        DrawableCompat.setTint(wrapped, tintColor);
    }

    public static void setScrollBehavior(final FloatingActionButton fab, RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public static String clean(String dirty) {
        if (dirty == null) {
            return null;
        }

        String clean = dirty.replaceAll("_", " ");

        boolean space = true;
        StringBuilder builder = new StringBuilder(clean);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public void colorize(final MovieAdapter.MovieHolder holder) {
        if (holder.posterHolder == null || holder.moviePanel == null || holder.posterHolder.getDrawable() == null)
            return;

        Bitmap bitmap = ((BitmapDrawable) holder.posterHolder.getDrawable()).getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getDarkVibrantSwatch();

                int bgColor = context.getResources().getColor(R.color.basic_light);
                int textColor = context.getResources().getColor(R.color.basic_dark);

                if (swatch != null) {
                    bgColor = swatch.getRgb();
                    textColor = swatch.getTitleTextColor();
                }

                holder.moviePanel.setBackgroundColor(bgColor);
                holder.movieTitle.setTextColor(textColor);

                setTint(holder.favourite, textColor);
                holder.textColor = textColor;
            }
        });
    }

    public void shareMovie(Movie movie) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, movie.toString());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.share_movie)));
    }

    public String getNextLoadingMessage() {
        String loading[] = context.getResources().getStringArray(R.array.loading_messages);
        return loading[(int) (Math.random() * loading.length)];
    }

}
