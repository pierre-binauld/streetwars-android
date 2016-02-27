package fr.streetgames.streetwars.app.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import fr.streetgames.streetwars.R;

import fr.streetgames.streetwars.app.fragments.MainActivityFragment;
import fr.streetgames.streetwars.app.fragments.WaterCodeFragment;
import fr.streetgames.streetwars.glide.CircleTransform;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mHeaderPhotoImageView;
    private ImageView mHeaderBackgroundImageView;
    private Fragment mFragment;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onViewCreated();

        mNavigationView.setNavigationItemSelectedListener(this);

        Glide.with(this)
                .load("http://images-cdn.9gag.com/photo/a1YmX32_700b.jpg")
                .transform(new CircleTransform(this))
                .into(mHeaderPhotoImageView);

        Glide.with(this)
                .load("http://androidspin.com/wp-content/uploads/2014/02/samsung_galaxy_s5_wallpaper___blue_version_by_shimmi1-d78444j-750x400.jpg")
                .into(mHeaderBackgroundImageView);

        // Manage fragment
        if (savedInstanceState == null) {
            switchToWaterCodeFragment();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mFragment = fragmentManager.findFragmentByTag(WaterCodeFragment.TAG);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_water_code:
                switchToWaterCodeFragment();
                break;
            case R.id.menu_contract:
                switchToContractFragment();
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid menu item id", item.getItemId()));
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void onViewCreated() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);

        View headerView = mNavigationView.getHeaderView(0);
        mHeaderPhotoImageView = (ImageView) headerView.findViewById(R.id.header_photo);
        mHeaderBackgroundImageView = (ImageView) headerView.findViewById(R.id.header_background);
    }

    public void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Initializing Drawer Layout and ActionBarToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_description_open, R.string.drawer_description_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        // Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // calling sync state is necessary or else your hamburger icon wont show up
        mDrawerToggle.syncState();
    }

    private void switchToWaterCodeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentByTag(WaterCodeFragment.TAG);
        if (null == mFragment) {
            mFragment = WaterCodeFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, mFragment, WaterCodeFragment.TAG)
                .commit();
    }

    private void switchToContractFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentByTag(MainActivityFragment.TAG);
        if (null == mFragment) {
            mFragment = new MainActivityFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, mFragment, MainActivityFragment.TAG)
                .commit();
    }

}
