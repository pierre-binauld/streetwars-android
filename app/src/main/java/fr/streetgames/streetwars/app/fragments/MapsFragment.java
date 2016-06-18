package fr.streetgames.streetwars.app.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.activities.MainActivity;
import fr.streetgames.streetwars.widget.TargetMarkerAdapter;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Target;

public class MapsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String TAG = "MapsFragment";

    private static final int PERMISSION_CODE_ACCESS_FINE_LOCATION = 0;

    private static final int PERMISSION_CODE_GET_CURRENT_LOCATION = 1;

    private GoogleMap mMap;

    private TargetMarkerAdapter mAdapter;

    private Toolbar mToolbar;

    private FloatingActionButton mMyLocation;

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
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mMyLocation = (FloatingActionButton) view.findViewById(R.id.fab_my_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();

        activity.setTitle(R.string.map_title);

        ActionBar actionBar = activity.setupToolbar(mToolbar);
        actionBar.setDisplayShowTitleEnabled(true);
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
            case PERMISSION_CODE_GET_CURRENT_LOCATION:
                if (permissions.length == 1 &&
                        Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0]) &&
                        PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    //noinspection MissingPermission
                    mMap.setMyLocationEnabled(true);
                    centerOnMyLocationWithPermissionGranted();
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            mMap.setPadding(
                    0,
                    TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics()),
                    0,
                    0
            );
        }

        mMyLocation.setOnClickListener(this);

        boolean checkPermission = ContextCompat
                .checkSelfPermission(
                        getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;
        if (checkPermission) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_CODE_ACCESS_FINE_LOCATION
            );
        }

        // TODO get from DB or resources or API
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_my_location:
                centerOnMyLocation();
                break;
        }
    }

    private void centerOnMyLocation() {

        boolean checkPermission = ContextCompat
                .checkSelfPermission(
                        getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;
        if (checkPermission) {
            centerOnMyLocationWithPermissionGranted();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_CODE_GET_CURRENT_LOCATION
            );
        }

    }

    @SuppressWarnings("MissingPermission")
    private void centerOnMyLocationWithPermissionGranted() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        if (location != null) {
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                            ),
                            17
                    )
            );
        }
    }

}
