package fr.streetgames.streetwars.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.app.fragments.FabFragment;
import fr.streetgames.streetwars.app.fragments.MainActivityFragment;
import fr.streetgames.streetwars.app.fragments.WaterCodeFragment;
import fr.streetgames.streetwars.picasso.CircleTransform;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FabFragment.SetupFabButtonListener {

    private Fragment mFragment;

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;

    private FloatingActionButton mFab;

    private ImageView mHeaderPhotoImageView;

    private ImageView mHeaderBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupNavigationView();

        Picasso.with(this)
                .load("http://images-cdn.9gag.com/photo/a1YmX32_700b.jpg")
                .transform(new CircleTransform())
                .into(mHeaderPhotoImageView);

        Picasso.with(this)
                .load("http://androidspin.com/wp-content/uploads/2014/02/samsung_galaxy_s5_wallpaper___blue_version_by_shimmi1-d78444j-750x400.jpg")
                .into(mHeaderBackgroundImageView);

        // Manage fragment
        if (savedInstanceState == null) {
           switchToWaterCodeFragment();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mFragment = fragmentManager.findFragmentByTag(
                    WaterCodeFragment.TAG
            );
        }
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

    private void bindViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        View headerView = mNavigationView.getHeaderView(0);
        mHeaderPhotoImageView = (ImageView) headerView.findViewById(R.id.header_photo);
        mHeaderBackgroundImageView = (ImageView) headerView.findViewById(R.id.header_background);
    }

    private void setupNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.content_authority, R.string.content_authority) {

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

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void setOnFabClickListener(View.OnClickListener listener) {
        mFab.setOnClickListener(listener);
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
