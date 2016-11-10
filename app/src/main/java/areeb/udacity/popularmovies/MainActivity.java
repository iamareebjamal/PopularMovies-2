package areeb.udacity.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import areeb.udacity.popularmovies.api.Sort;
import areeb.udacity.popularmovies.fragment.DetailFragment;
import areeb.udacity.popularmovies.fragment.MoviesFragment;
import areeb.udacity.popularmovies.model.Movie;
import areeb.udacity.popularmovies.model.Movies;
import areeb.udacity.popularmovies.utils.MovieSelector;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieSelector {

    private static final String FRAGMENT_KEY = "movie_fragment";

    private static MoviesFragment moviesFragment;
    private static DetailFragment detailFragment;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        } else {
            moviesFragment = (MoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        }

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);

    }

    public void setTitle(String title) {
        toolbar.setTitle(Utils.clean(title));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            moviesFragment.loadMovies(new Movies(realm.copyFromRealm(realm.where(Movie.class).findAll())));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, moviesFragment);
    }

    @Override
    public void onSelect(Movie movie) {
        detailFragment.setMovie(movie);
    }

    public static boolean isDualPane(){
        return (detailFragment==null)?false:detailFragment.isInLayout();
    }
}
