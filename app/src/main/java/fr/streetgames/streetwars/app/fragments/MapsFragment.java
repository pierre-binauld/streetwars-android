package fr.streetgames.streetwars.app.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
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
import android.util.Log;
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

public class MapsFragment
        extends Fragment
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        MapsDetailBottomSheetDelegate.BottomSheetCallback {

    public static final String TAG = "MapsFragment";

    private static final int PERMISSION_CODE_ACCESS_FINE_LOCATION = 0;

    private static final int PERMISSION_CODE_GET_CURRENT_LOCATION = 1;

    private GoogleMap mMap;

    private View mEmptyInfoView;

    private Toolbar mToolbar;

    private FloatingActionButton mMyLocationButton;

    private MapsDetailBottomSheetDelegate mMapDetailBottomSheetDelegate;

    private Cursor mCursor;

    private ColorStateList mFabStateList;

    private ColorStateList mAccentFabStateList;

    private int mMapPaddingTop;

    public static Fragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapDetailBottomSheetDelegate = new MapsDetailBottomSheetDelegate(this, savedInstanceState);
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
        mMyLocationButton = (FloatingActionButton) view.findViewById(R.id.fab_my_location);
//        mMyLocationButton.hide();

        mMapDetailBottomSheetDelegate.onViewCreated(view, savedInstanceState);
        mMapDetailBottomSheetDelegate.setBottomSheetCallback(this);

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

        mAccentFabStateList = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mFabStateList = ColorStateList.valueOf(Color.WHITE);

        mMapPaddingTop = 0;
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            mMapPaddingTop = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
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
        mMap.getUiSettings().setMapToolbarEnabled(true); // false
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // false
        mMap.getUiSettings().setZoomControlsEnabled(true); // false
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            mMap.setPadding(
                    0,
                    mMapPaddingTop,
                    0,
                    0
            );
        }

        mMyLocationButton.setOnClickListener(this);

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
        mCursor = cursor;
        if (null != mCursor) {
            int i = 0;
            mCursor.moveToFirst();
            do {
                // Bind address markers
                final LatLng coord = new LatLng(
                        mCursor.getDouble(AddressProjection.QUERY_ADDRESS_LATITUDE),
                        mCursor.getDouble(AddressProjection.QUERY_ADDRESS_LONGITUDE)
                );

                Marker marker = map.addMarker(
                        new MarkerOptions()
                                .position(coord)
                                .icon(BitmapDescriptorFactory.defaultMarker(MarkerUtils.HUE[i % MarkerUtils.HUE.length]))
                );
                marker.setTag(mCursor.getPosition());

                i++;
            } while (mCursor.moveToNext());
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
        int cursorPosition = (int) marker.getTag();
        if (mCursor.moveToPosition(cursorPosition)) {
            int count = mCursor.getInt(AddressProjection.QUERY_ADDRESS_COUNT);
            if (count <= 1 && false) {
                mMapDetailBottomSheetDelegate.closeBottomSheet();
                long playerId = mCursor.getLong(AddressProjection.QUERY_ADDRESS_PLAYER_ID);
                TargetBottomSheetDialogFragment.show(getContext(), playerId);
            }
            else {
                String address = mCursor.getString(AddressProjection.QUERY_ADDRESS);
                LatLng position = marker.getPosition();
                mMapDetailBottomSheetDelegate.searchFor(address, position.latitude, position.longitude);
            }
        }
        else {
            mMapDetailBottomSheetDelegate.closeBottomSheet();
            Log.e(TAG, "onMarkerClick: mCursor can't move to " + cursorPosition);
        }

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMapDetailBottomSheetDelegate.closeBottomSheet();
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        mMap.setPadding(
                0,
                mMapPaddingTop,
                0,
                (int) (slideOffset * bottomSheet.getMeasuredHeight())
        );

        if (slideOffset > 0.5) {
            mMyLocationButton.setBackgroundTintList(mAccentFabStateList);
            mMyLocationButton.setImageResource(R.drawable.ic_my_location_white_24dp);
        }
        else {
            mMyLocationButton.setBackgroundTintList(mFabStateList);
            mMyLocationButton.setImageResource(R.drawable.ic_my_location_grey_24dp);
        }
    }

    public interface AddressProjection {

        String[] PROJECTION = new String[]{
                Address.ADDRESS,
                Address.ADDRESS_LATITUDE,
                Address.ADDRESS_LONGITUDE,
                Address.ADDRESS_COUNT,
                Address.PLAYER_ID
        };

        int QUERY_ADDRESS = 0;

        int QUERY_ADDRESS_LATITUDE = 1;

        int QUERY_ADDRESS_LONGITUDE = 2;

        int QUERY_ADDRESS_COUNT = 3;

        int QUERY_ADDRESS_PLAYER_ID = 4;
    }
}
