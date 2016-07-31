package areeb.udacity.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import areeb.udacity.popularmovies.fragment.DetailFragment;
import areeb.udacity.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private static final String FRAGMENT_KEY = "Fragment";
    private DetailFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(Movie.TAG)) {
            movie = getIntent().getParcelableExtra(Movie.TAG);
            if(savedInstanceState!=null && savedInstanceState.containsKey(FRAGMENT_KEY))
                newFragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            else
                newFragment = DetailFragment.newInstance(movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, newFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, newFragment);
    }

    public void setBackButton(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
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
