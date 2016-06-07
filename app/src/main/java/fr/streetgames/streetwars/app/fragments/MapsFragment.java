package fr.streetgames.streetwars.app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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
import fr.streetgames.streetwars.widget.TargetMarkerAdapter;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Target;

public class MapsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "MapsFragment";

    private static final int PERMISSION_CODE_ACCESS_FINE_LOCATION = 0;

    private GoogleMap mMap;

    private TargetMarkerAdapter mAdapter;

    public static Fragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TargetMarkerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter.onCreateView(inflater, container);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_ACCESS_FINE_LOCATION:
                if (permissions.length == 1 &&
                        Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0]) &&
                        PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    //noinspection MissingPermission
                    mMap.setMyLocationEnabled(true);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown permission request code: " + requestCode);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(mAdapter);

        boolean checkPermission = ContextCompat
                .checkSelfPermission(
                        getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;
        if (checkPermission) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_CODE_ACCESS_FINE_LOCATION
            );
        }


        // TODO get from DB
        LatLng lyon = new LatLng(45.759290, 4.851665);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lyon));

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
                TargetMarkerAdapter.TargetProjection.PROJECTION,
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
        mAdapter.initMarkers(mMap, cursor);
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

}
