package fr.streetgames.streetwars.app.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
import fr.streetgames.streetwars.api.StreetWarsPlayer;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.utils.IntentUtils;

public class TargetBottomSheetDialogFragment extends BottomSheetDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "TargetBottomSheetDialogFragment";

    public static final String ARG_PLAYER_ID = "arg:player_id";

    private static final String STATE_PLAYER_ID = "state:player_id";

    private long mPlayerId = StreetWarsPlayer.NO_ID;

    private Cursor mCursor;

    private TextView mNameTextView;

    private TextView mAliasTextView;

    private TextView mExtraTextView;

    private TextView mHomeTextView;

    private TextView mWorkTextView;

    private ImageButton mPhotoImageButton;

    private View mHomeView;

    private View mWorkView;

    private ImageView mHomeImageView;

    private TextView mHomeLabelTextView;

    private ImageView mWorkImageView;

    private TextView mWorkLabelTextView;

    public static void show(Context context, long playerId) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        TargetBottomSheetDialogFragment dialog = TargetBottomSheetDialogFragment.newInstance(playerId);
        dialog.show(fragmentManager, TargetBottomSheetDialogFragment.TAG);
    }

    public static TargetBottomSheetDialogFragment newInstance(long playerId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PLAYER_ID, playerId);
        TargetBottomSheetDialogFragment fragment = new TargetBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_PLAYER_ID, mPlayerId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            mPlayerId = savedInstanceState.getLong(STATE_PLAYER_ID);
        }
        else {
            Bundle args = getArguments();
            mPlayerId = args.getLong(ARG_PLAYER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet_target, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mHomeView = view.findViewById(R.id.target_container_home);
        mWorkView = view.findViewById(R.id.target_container_work);
        mPhotoImageButton = (ImageButton) view.findViewById(R.id.target_photo);
        mHomeImageView = (ImageView) view.findViewById(R.id.target_ic_home);
        mWorkImageView = (ImageView) view.findViewById(R.id.target_ic_work);
        mNameTextView = (TextView) view.findViewById(R.id.target_name);
        mAliasTextView = (TextView) view.findViewById(R.id.target_alias);
        mExtraTextView = (TextView) view.findViewById(R.id.target_extra);
        mHomeTextView = (TextView) view.findViewById(R.id.target_home);
        mWorkTextView = (TextView) view.findViewById(R.id.target_work);
        mHomeLabelTextView = (TextView) view.findViewById(R.id.target_label_home);
        mWorkLabelTextView = (TextView) view.findViewById(R.id.target_label_work);

        mPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoClick();
            }
        });

        mHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeCLick();
            }
        });

        mWorkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkCLick();
            }
        });
    }

    private void onPhotoClick(){
        IntentUtils.startImageActivity(getContext(), mCursor.getString(TargetProjection.QUERY_PHOTO));
    }

    private void onHomeCLick(){
        String address = String.valueOf(mCursor.getString(TargetProjection.QUERY_HOME));
        IntentUtils.startGeoActivity(getContext(), address);
    }

    private void onWorkCLick(){
        String address = String.valueOf(mCursor.getString(TargetProjection.QUERY_WORK));
        IntentUtils.startGeoActivity(getContext(), address);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (null != view) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
            behavior.setPeekHeight(mPhotoImageButton.getMeasuredHeight());

            mPhotoImageButton.addOnLayoutChangeListener(new OnPhotoLayoutChangeListener(behavior));
        }

        queryTargets();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_target:
                return onCreateQueryTargetsLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private Loader<Cursor> onCreateQueryTargetsLoader(@NonNull Bundle args) {
        return new CursorLoader(
                getContext(),
                StreetWarsContract.Target.CONTENT_URI,
                TargetProjection.PROJECTION,
                StreetWarsContract.Player.ID + " = ?",
                new String[] {String.valueOf(mPlayerId)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_target:
                onQueryTargetLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void onQueryTargetLoadFinished(Cursor cursor) {
        mCursor = cursor;

        if (null != mCursor && mCursor.moveToFirst()) {
            mNameTextView.setText(
                    getResources().getString(
                            R.string.util_name,
                            mCursor.getString(TargetProjection.QUERY_FIRST_NAME),
                            mCursor.getString(TargetProjection.QUERY_LAST_NAME)
                    )
            );
            mAliasTextView.setText(mCursor.getString(TargetProjection.QUERY_ALIAS));
            mExtraTextView.setText(mCursor.getString(TargetProjection.QUERY_EXTRA));
            mHomeTextView.setText(mCursor.getString(TargetProjection.QUERY_HOME));
            mWorkTextView.setText(mCursor.getString(TargetProjection.QUERY_WORK));

            String photoUrl = mCursor.getString(TargetProjection.QUERY_PHOTO);
            Picasso.with(getContext())
                    .load(photoUrl)
                    .placeholder(R.drawable.placeholder_user_full_336dp)
                    .into(mPhotoImageButton);

            @StreetWarsJobCategory.JobCategory int jobCat = mCursor.getInt(TargetProjection.QUERY_JOB_CATEGORY);
            switch (jobCat) {
                case StreetWarsJobCategory.NO_jOB:
                    mHomeView.setVisibility(View.VISIBLE);
                    mWorkView.setVisibility(View.GONE);
                    mHomeImageView.setImageResource(R.drawable.ic_no_job_accent_24dp);
                    mHomeLabelTextView.setText(R.string.target_home);
                    break;
                case StreetWarsJobCategory.STUDENT:
                    mHomeView.setVisibility(View.VISIBLE);
                    mWorkView.setVisibility(View.VISIBLE);
                    mHomeImageView.setImageResource(R.drawable.ic_home_accent_24dp);
                    mWorkImageView.setImageResource(R.drawable.ic_school_accent_24dp);
                    mHomeLabelTextView.setText(R.string.target_home);
                    mWorkLabelTextView.setText(R.string.target_school);
                    break;
                case StreetWarsJobCategory.WORKER:
                    mHomeView.setVisibility(View.VISIBLE);
                    mWorkView.setVisibility(View.VISIBLE);
                    mHomeImageView.setImageResource(R.drawable.ic_home_accent_24dp);
                    mWorkImageView.setImageResource(R.drawable.ic_work_accent_24dp);
                    mHomeLabelTextView.setText(R.string.target_home);
                    mWorkLabelTextView.setText(R.string.target_work);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_target:
                mCursor = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void queryTargets() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_target, null, this);
    }

    private static class OnPhotoLayoutChangeListener implements View.OnLayoutChangeListener {

        @NonNull
        private BottomSheetBehavior mBehavior;

        private OnPhotoLayoutChangeListener(@NonNull BottomSheetBehavior behavior) {
            mBehavior = behavior;
        }

        @Override
        public void onLayoutChange(
                View v,
                int left,
                int top,
                int right,
                int bottom,
                int oldLeft,
                int oldTop,
                int oldRight,
                int oldBottom) {
            mBehavior.setPeekHeight(bottom - top);
        }
    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                StreetWarsContract.Target.TEAM_NAME,
                StreetWarsContract.Target.ID,
                StreetWarsContract.Target.FIRST_NAME,
                StreetWarsContract.Target.LAST_NAME,
                StreetWarsContract.Target.ALIAS,
                StreetWarsContract.Target.PHOTO,
                StreetWarsContract.Target.KILL_COUNT,
                StreetWarsContract.Target.JOB_CATEGORY,
                StreetWarsContract.Target.HOME,
                StreetWarsContract.Target.WORK,
                StreetWarsContract.Target.EXTRA
        };

        int QUERY_TEAM_NAME = 0;

        int QUERY_ID = 1;

        int QUERY_FIRST_NAME = 2;

        int QUERY_LAST_NAME = 3;

        int QUERY_ALIAS = 4;

        int QUERY_PHOTO = 5;

        int QUERY_KILL_COUNT = 6;

        int QUERY_JOB_CATEGORY = 7;

        int QUERY_HOME = 8;

        int QUERY_WORK = 9;

        int QUERY_EXTRA = 10;
    }
}
