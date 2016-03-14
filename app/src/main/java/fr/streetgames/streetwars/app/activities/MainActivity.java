package fr.streetgames.streetwars.app.activities;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.R;

import fr.streetgames.streetwars.app.fragments.ContractFragment;
import fr.streetgames.streetwars.app.fragments.WaterCodeFragment;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.glide.CircleTransform;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mDrawerBackgroundImageView;
    private ImageView mPlayerPhotoImageView;
    private TextView mPlayerNameTextView;
    private TextView mPlayerAliasTextView;

    private Fragment mFragment;

    private ActionBarDrawerToggle mDrawerToggle;
    private Cursor mPlayerCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onViewCreated();

        mNavigationView.setNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(BuildConfig.LOCATION_HEADER_DRAWER)
                .placeholder(R.color.colorPrimaryDark)
                .centerCrop()
                .into(mDrawerBackgroundImageView);

        // Reattach loaders
        LoaderManager loaderManager = getSupportLoaderManager();
        if(loaderManager.getLoader(R.id.loader_query_player) != null) {
            loaderManager.initLoader(R.id.loader_query_player, null, this);
        }

        // Run loaders
        queryPlayer();

        // Manage fragment
        if (savedInstanceState == null) {
            switchToWaterCodeFragment();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mFragment = fragmentManager.findFragmentByTag(WaterCodeFragment.TAG);
        }
    }

    private void onViewCreated() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);

        View headerView = mNavigationView.getHeaderView(0);
        mDrawerBackgroundImageView = (ImageView) headerView.findViewById(R.id.header_background);
        mPlayerPhotoImageView = (ImageView) headerView.findViewById(R.id.header_photo);
        mPlayerNameTextView = (TextView) headerView.findViewById(R.id.header_name);
        mPlayerAliasTextView = (TextView) headerView.findViewById(R.id.header_alias);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_player:
                return onCreateQueryPlayerLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private Loader<Cursor> onCreateQueryPlayerLoader(Bundle args) {
        return new CursorLoader(
                this,
                StreetWarsContract.Player.CONTENT_URI,
                PlayerProjection.PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_player:
                onQueryPlayerLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void onQueryPlayerLoadFinished(Cursor cursor) {
        mPlayerCursor = cursor;
        updatePlayerInfo();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_player:
                mPlayerCursor = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void queryPlayer() {
        LoaderManager loaderManager = getSupportLoaderManager();
        if(loaderManager.getLoader(R.id.loader_query_player) == null) {
            loaderManager.initLoader(R.id.loader_query_player, null, this);
        }
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
        mFragment = fragmentManager.findFragmentByTag(ContractFragment.TAG);
        if (null == mFragment) {
            mFragment = new ContractFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, mFragment, ContractFragment.TAG)
                .commit();
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

    private void updatePlayerInfo() {
        if(mPlayerCursor != null && mPlayerCursor.moveToFirst()) {
            Glide.with(this)
                    .load(mPlayerCursor.getString(PlayerProjection.QUERY_PHOTO))
                    .placeholder(R.drawable.drawer_photo_placeholder)
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(mPlayerPhotoImageView);

            mPlayerNameTextView.setText(
                    getResources()
                            .getString(
                                    R.string.drawer_player_name,
                                    mPlayerCursor.getString(PlayerProjection.QUERY_FIRST_NAME),
                                    mPlayerCursor.getString(PlayerProjection.QUERY_LAST_NAME)
                            )
            );

            mPlayerAliasTextView.setText(
                    mPlayerCursor.getString(PlayerProjection.QUERY_ALIAS)
            );
        }
    }

    public interface PlayerProjection {

        String[] PROJECTION = new String[] {
                StreetWarsContract.Player.ID,
                StreetWarsContract.Player.FIRST_NAME,
                StreetWarsContract.Player.LAST_NAME,
                StreetWarsContract.Player.ALIAS,
                StreetWarsContract.Player.PHOTO
        };

        int QUERY_ID = 0;

        int QUERY_FIRST_NAME = 1;

        int QUERY_LAST_NAME = 2;

        int QUERY_ALIAS = 3;

        int QUERY_PHOTO = 4;
    }
}
