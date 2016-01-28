package fr.streetgames.streetwars.app.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.app.fragments.WaterCodeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();

        mNavigationView.setNavigationItemSelectedListener(this);

        // Manage fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragment = WaterCodeFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.content_fragment, mFragment, WaterCodeFragment.TAG)
                    .commit();
        }
        else {
            mFragment = fragmentManager.findFragmentByTag(
                    WaterCodeFragment.TAG
            );
        }
    }

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_water_code:

                return true;
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid menu item id", item.getItemId()));
        }
    }

    public void bindViews() {
        mNavigationView = (NavigationView) findViewById(R.id.drawer);
    }
}
