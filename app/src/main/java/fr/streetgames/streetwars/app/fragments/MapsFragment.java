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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.activities.MainActivity;
import fr.streetgames.streetwars.app.delegate.MapsDetailBottomSheetDelegate;
import fr.streetgames.streetwars.content.contract.StreetWarsContract.Address;
import fr.streetgames.streetwars.utils.MarkerUtils;

public class MapsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.InfoWindowAdapter {

    public static final String TAG = "MapsFragment";

    private static final int PERMISSION_CODE_ACCESS_FINE_LOCATION = 0;

    private static final int PERMISSION_CODE_GET_CURRENT_LOCATION = 1;

    private GoogleMap mMap;

    private View mEmptyInfoView;

    private MarkerOptions mMarkerOptions;

    private Toolbar mToolbar;

    private FloatingActionButton mMyLocation;

    private MapsDetailBottomSheetDelegate mMapDetailBottomSheetDelegate;

    public static Fragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapDetailBottomSheetDelegate = new MapsDetailBottomSheetDelegate(this, savedInstanceState);
        mMarkerOptions = new MarkerOptions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEmptyInfoView = new View(getContext());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mMyLocation = (FloatingActionButton) view.findViewById(R.id.fab_my_location);

        mMapDetailBottomSheetDelegate.onViewCreated(view, savedInstanceState);

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
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(this);
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

    public void initMarkers(GoogleMap map, Cursor cursor) {
        if (null != cursor) {
            int i = 0;
            cursor.moveToFirst();
            do {
                // Bind address markers
                final LatLng coord = new LatLng(
                        cursor.getDouble(AddressProjection.QUERY_ADDRESS_LATITUDE),
                        cursor.getDouble(AddressProjection.QUERY_ADDRESS_LONGITUDE)
                );

                map.addMarker(
                        mMarkerOptions
                                .title(cursor.getString(AddressProjection.QUERY_ADDRESS))
                                .position(coord)
                                .icon(BitmapDescriptorFactory.defaultMarker(MarkerUtils.HUE[i % MarkerUtils.HUE.length]))
                );

                i++;
            } while (cursor.moveToNext());
        }
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
                Address.CONTENT_URI,
                AddressProjection.PROJECTION,
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
        initMarkers(mMap, cursor);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng position = marker.getPosition();
        mMapDetailBottomSheetDelegate.searchFor(marker.getTitle(), position.latitude, position.longitude);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMapDetailBottomSheetDelegate.closeBottomSheet();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return mEmptyInfoView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public interface AddressProjection {

        String[] PROJECTION = new String[]{
                Address.ADDRESS,
                Address.ADDRESS_LATITUDE,
                Address.ADDRESS_LONGITUDE
        };

        int QUERY_ADDRESS = 0;

        int QUERY_ADDRESS_LATITUDE = 1;

        int QUERY_ADDRESS_LONGITUDE = 2;
    }
}
