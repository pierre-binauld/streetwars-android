package fr.streetgames.streetwars.app.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.streetgames.streetwars.R;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapsFragment";

    private GoogleMap mMap;

    private Cursor mCursor;

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

        mMap.addMarker(
                new MarkerOptions()
                        .position(lyon)
                        .title("Marker in Lyon")
                        .icon(
                                BitmapDescriptorFactory
                                        .defaultMarker(
                                                BitmapDescriptorFactory.HUE_YELLOW
                                        )
                        )
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lyon));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

}
