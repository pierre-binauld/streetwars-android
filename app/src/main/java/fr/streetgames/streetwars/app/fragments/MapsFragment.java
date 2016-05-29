package fr.streetgames.streetwars.app.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import fr.streetgames.streetwars.R;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Target;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "MapsFragment";

    private GoogleMap mMap;

    public static Fragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection WrongConstant
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng lyon = new LatLng(45.764043, 4.835659);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lyon));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        queryTargets();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_targets:
                return onCreateQueryTargetsLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private Loader<Cursor> onCreateQueryTargetsLoader(Bundle args) {
        return new CursorLoader(
                getContext(),
                Target.CONTENT_URI.buildUpon()
                        .appendQueryParameter(Target.PARAM_SHOW_TEAM, "false")
                        .build(),
                TargetProjection.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_targets:
                onQueryTargetsLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void onQueryTargetsLoadFinished(@Nullable Cursor cursor) {
        if (null != cursor) {
            cursor.moveToFirst();
            do {


            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_targets:
                // Do nothing
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void queryTargets() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_targets, null, this);
    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                Target.ID,
                Target.TEAM_NAME,
                Target.FIRST_NAME,
                Target.LAST_NAME,
                Target.ALIAS,
                Target.PHOTO,
                Target.KILL_COUNT,
                Target.JOB_CATEGORY,
                Target.HOME,
                Target.WORK
        };

        int QUERY_ID = 0;

        int QUERY_TEAM_NAME = 1;

        int QUERY_FIRST_NAME = 2;

        int QUERY_LAST_NAME = 3;

        int QUERY_ALIAS = 4;

        int QUERY_PHOTO = 5;

        int QUERY_KILL_COUNT = 6;

        int QUERY_JOB_CATEGORY = 7;

        int QUERY_HOME = 8;

        int QUERY_WORK = 9;
    }

}
