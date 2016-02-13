package fr.streetgames.streetwars.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.app.fragments.FabFragment;
import fr.streetgames.streetwars.app.fragments.WaterCodeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FabFragment.SetupFabButtonListener {

    private Fragment mFragment;

    private NavigationView mNavigationView;

    private FloatingActionButton mFab;

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
        mFab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void setOnFabClickListener(View.OnClickListener listener) {
        mFab.setOnClickListener(listener);
    }
}
