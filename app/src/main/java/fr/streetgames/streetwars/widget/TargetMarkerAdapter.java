package fr.streetgames.streetwars.widget;

import android.database.Cursor;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.picasso.CircleTransform;
import fr.streetgames.streetwars.utils.MarkerUtils;

public class TargetMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private static final int HOME = 0;

    private static final int WORK = 1;

    @IntDef({
            HOME,
            WORK
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface LocationType {

    }


    @NonNull
    private final MarkerOptions mMarkerOptions;

    @Nullable
    private View mMarkerInfoView;

    @Nullable
    private Cursor mCursor;

    @NonNull
    private final Map<String, Integer> mCursorPositions;

    private final Map<String, Integer> mLocationTypes;

    public TargetMarkerAdapter() {
        mMarkerOptions = new MarkerOptions();
        mCursorPositions = new HashMap<>();
        mLocationTypes = new HashMap<>();
    }

    public void onCreateView(LayoutInflater inflater, ViewGroup container) {
        mMarkerInfoView = inflater.inflate(R.layout.info_view_target, container, false);
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    public void initMarkers(GoogleMap map, Cursor cursor) {
        mCursor = cursor;
        if (null != mCursor) {
            int i = 0;
            mCursor.moveToFirst();
            do {
                @StreetWarsJobCategory.JobCategory int jobCat = mCursor.getInt(TargetProjection.QUERY_JOB_CATEGORY);

                // Bind home marker
                final LatLng homeCoord = new LatLng(
                        mCursor.getDouble(TargetProjection.QUERY_HOME_LATITUDE),
                        mCursor.getDouble(TargetProjection.QUERY_HOME_LONGITUDE)
                );

                String title = mCursor.getString(TargetProjection.QUERY_FIRST_NAME);

                Marker marker = map.addMarker(
                        mMarkerOptions
                                .position(homeCoord)
                                .title(title)
                                .icon(BitmapDescriptorFactory.defaultMarker(MarkerUtils.HUE[i]))
                );
                mCursorPositions.put(marker.getId(), i);
                mLocationTypes.put(marker.getId(), HOME);

                // Bind work marker
                if (StreetWarsJobCategory.NO_jOB != jobCat) {
                    final LatLng workCoord = new LatLng(
                            mCursor.getDouble(TargetProjection.QUERY_WORK_LATITUDE),
                            mCursor.getDouble(TargetProjection.QUERY_WORK_LONGITUDE)
                    );

                    title = mCursor.getString(TargetProjection.QUERY_FIRST_NAME);

                    marker = map.addMarker(
                            mMarkerOptions
                                    .position(workCoord)
                                    .title(title)
                                    .icon(BitmapDescriptorFactory.defaultMarker(MarkerUtils.HUE[i]))
                    );
                    mCursorPositions.put(marker.getId(), i);
                    mLocationTypes.put(marker.getId(), WORK);
                }

                i++;
            } while (mCursor.moveToNext());
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        assert null != mMarkerInfoView;

        if (null != mCursor && mCursor.moveToPosition(mCursorPositions.get(marker.getId()))) {

            ImageView targetLocationIcon = (ImageView) mMarkerInfoView.findViewById(R.id.target_ic_location); // TODO Field
            @StreetWarsJobCategory.JobCategory int jobCat = mCursor.getInt(TargetProjection.QUERY_JOB_CATEGORY);
            @LocationType int locationType = mLocationTypes.get(marker.getId());
            switch (locationType) {
                default:
                case HOME:
                    switch (jobCat) {
                        case StreetWarsJobCategory.NO_jOB:
                            targetLocationIcon.setImageResource(R.drawable.ic_no_job_24dp);
                            break;
                        case StreetWarsJobCategory.STUDENT:
                            targetLocationIcon.setImageResource(R.drawable.ic_home_24dp);
                            break;
                        default:
                        case StreetWarsJobCategory.WORKER:
                            targetLocationIcon.setImageResource(R.drawable.ic_home_24dp);
                            break;
                    }
                    break;
                case WORK:
                    switch (jobCat) {
                        case StreetWarsJobCategory.NO_jOB:
                            throw new IllegalArgumentException("You try to set a work location to a target without job.");
                        case StreetWarsJobCategory.STUDENT:
                            targetLocationIcon.setImageResource(R.drawable.ic_school_24dp);
                            break;
                        default:
                        case StreetWarsJobCategory.WORKER:
                            targetLocationIcon.setImageResource(R.drawable.ic_work_24dp);
                            break;
                    }
                    break;
            }

            TextView targetTitle = (TextView) mMarkerInfoView.findViewById(R.id.target_title); // TODO Put it into fields
            targetTitle.setText(mCursor.getString(TargetProjection.QUERY_FIRST_NAME));

            ImageView targetPhoto = (ImageView) mMarkerInfoView.findViewById(R.id.target_photo);
            Picasso.with(mMarkerInfoView.getContext())
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .transform(new CircleTransform()) // TODO Field
                    .placeholder(R.drawable.placeholder_user_round_48dp)
                    .into(targetPhoto);

            return mMarkerInfoView;
        }
        return null;
    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                StreetWarsContract.Target.ID,
                StreetWarsContract.Target.TEAM_NAME,
                StreetWarsContract.Target.FIRST_NAME,
                StreetWarsContract.Target.LAST_NAME,
                StreetWarsContract.Target.ALIAS,
                StreetWarsContract.Target.PHOTO,
                StreetWarsContract.Target.KILL_COUNT,
                StreetWarsContract.Target.JOB_CATEGORY,
                StreetWarsContract.Target.HOME,
                StreetWarsContract.Target.HOME_LATITUDE,
                StreetWarsContract.Target.HOME_LONGITUDE,
                StreetWarsContract.Target.WORK,
                StreetWarsContract.Target.WORK_LATITUDE,
                StreetWarsContract.Target.WORK_LONGITUDE
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

        int QUERY_HOME_LATITUDE = 9;

        int QUERY_HOME_LONGITUDE = 10;

        int QUERY_WORK = 11;

        int QUERY_WORK_LATITUDE = 12;

        int QUERY_WORK_LONGITUDE = 13;
    }

}
