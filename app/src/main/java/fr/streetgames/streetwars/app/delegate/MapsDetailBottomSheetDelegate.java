package fr.streetgames.streetwars.app.delegate;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.delegate.base.CursorLoaderFragmentDelegate;
import fr.streetgames.streetwars.app.fragments.TargetBottomSheetDialogFragment;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.database.PlayerColumns;
import fr.streetgames.streetwars.widget.MapsPlayerAdapter;

public class MapsDetailBottomSheetDelegate extends CursorLoaderFragmentDelegate implements MapsPlayerAdapter.OnItemClickListener {

    public interface BottomSheetCallback {
        void onStateChanged(@NonNull View bottomSheet, int newState);

        void onSlide(@NonNull View bottomSheet, float slideOffset);
    }

    private static final String TAG = "MapsDetailBottomSheetDelegate";

    private static final String LOADER_LATITUDE = "loader:latitude";

    private static final String LOADER_LONGITUDE = "loader:longitude";

    private final Context mContext;

    private final Fragment mFragment;

    private BottomSheetBehavior mBehavior;

    private TextView mTitle;

    private RecyclerView mRecyclerView;

    private MapsPlayerAdapter mAdapter;

    @Nullable
    private BottomSheetCallback mBottomSheetCallback;

    public MapsDetailBottomSheetDelegate(Fragment fragment, Bundle savedInstanceState) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        mAdapter = new MapsPlayerAdapter();
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        View bottomSheet = view.findViewById(R.id.maps_detail_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottomSheet);

        mTitle = (TextView) view.findViewById(R.id.maps_detail_title);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.maps_detail_targets);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                MapsDetailBottomSheetDelegate.this.onStateChanged(bottomSheet, newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                MapsDetailBottomSheetDelegate.this.onSlide(bottomSheet, slideOffset);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, @NonNull Bundle data) {
        switch (id) {
            case R.id.loader_query_player:
                return onCreatePlayerLoader(data);
            default:
                return super.onCreateLoader(id, data);
        }
    }

    private Loader<Cursor> onCreatePlayerLoader(@NonNull Bundle data) {
        String selection = null;
        String[] selectionArgs = null;
        if (data.containsKey(LOADER_LATITUDE) && data.containsKey(LOADER_LONGITUDE)) {
            String latitude = String.valueOf(data.getDouble(LOADER_LATITUDE));
            String longitude = String.valueOf(data.getDouble(LOADER_LONGITUDE));

            selection = PlayerColumns.HOME_LATITUDE + " = ? AND " +
                    PlayerColumns.HOME_LONGITUDE + " = ? OR " +
                    PlayerColumns.WORK_LATITUDE + " = ? AND " +
                    PlayerColumns.WORK_LONGITUDE + " = ? ";

            selectionArgs = new String[] {
                    latitude,
                    longitude,
                    latitude,
                    longitude
            };
        }

        String order = PlayerColumns.TEAM_ID;

        return new CursorLoader(
                mContext,
                StreetWarsContract.Player.CONTENT_URI,
                MapsPlayerAdapter.PlayerProjection.PROJECTION,
                selection,
                selectionArgs,
                order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_player:
                onPlayerLoaderFinished(cursor);
                break;
            default:
                super.onLoadFinished(loader, cursor);
        }
    }

    private void onPlayerLoaderFinished(@Nullable Cursor cursor) {
        mAdapter.setCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_player:
                onPlayerLoaderReset(null);
                break;
            default:
                super.onLoaderReset(loader);
        }
    }

    private void onPlayerLoaderReset(Cursor cursor) {
        mAdapter.setCursor(null);
    }

    public void searchFor(@NonNull String address, double latitude, double longitude) {
        mTitle.setText(address);
        mAdapter.setCursor(null);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        Bundle args = new Bundle(2);
        args.putDouble(LOADER_LATITUDE, latitude);
        args.putDouble(LOADER_LONGITUDE, longitude);
        mFragment.getLoaderManager().restartLoader(
                R.id.loader_query_player,
                args,
                this);
    }

    public void closeBottomSheet() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
            mTitle.setText("");
            mAdapter.setCursor(null);
        }

        if (null != mBottomSheetCallback) {
            mBottomSheetCallback.onStateChanged(bottomSheet, newState);
        }
    }

    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (null != mBottomSheetCallback) {
            mBottomSheetCallback.onSlide(bottomSheet, slideOffset);
        }
    }

    @Override
    public void onItemClickListener(MapsPlayerAdapter.PlayerViewHolder holder) {
        TargetBottomSheetDialogFragment.show(mFragment.getContext(), holder.playerId);
    }

    public void setBottomSheetCallback(BottomSheetCallback bottomSheetCallback) {
        mBottomSheetCallback = bottomSheetCallback;
    }
}
