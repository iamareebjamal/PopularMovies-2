package areeb.udacity.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import areeb.udacity.popularmovies.api.Sort;
import areeb.udacity.popularmovies.fragment.MoviesFragment;
import areeb.udacity.popularmovies.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_KEY = "movie_fragment";

    private MoviesFragment moviesFragment;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            moviesFragment = MoviesFragment.newInstance(Sort.POPULAR);
        } else {
            moviesFragment = (MoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_frame, moviesFragment)
                .commit();
    }

    public void setTitle(String title) {
        toolbar.setTitle(Utils.clean(title));
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, moviesFragment);
    }
}
