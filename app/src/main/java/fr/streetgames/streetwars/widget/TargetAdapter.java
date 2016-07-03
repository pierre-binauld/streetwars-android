package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.streetgames.streetwars.app.fragments.TargetBottomSheetDialogFragment;
import fr.streetgames.streetwars.database.RowTypeColumns;
import fr.streetgames.streetwars.utils.IntentUtils;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Target;

public abstract class TargetAdapter extends CursorAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TargetAdapter";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Target.TYPE_TEAM,
            Target.TYPE_TARGET
    })
    public @interface ViewType {
    }

    @NonNull
    protected Context mContext;

    @NonNull
    protected Resources mResources;

    @Nullable
    private BottomSheetDialog mBottomSheetDialog;

    public TargetAdapter(@NonNull Context context) {
        mContext = context;
        mResources = mContext.getResources();
    }

    @CallSuper
    @Override
    public int getItemViewType(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            return mCursor.getInt(TargetProjection.QUERY_ROW_TYPE) == Target.TYPE_TEAM
                    ? Target.TYPE_TEAM
                    : Target.TYPE_TARGET;
        }
        else {
            throw new IllegalArgumentException("Position #" + position + " is not reachable.");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewType int viewType) {
        switch (viewType) {
            case Target.TYPE_TARGET:
                return onCreateTargetViewHolder(parent);
            case Target.TYPE_TEAM:
                return onCreateTeamViewHolder(parent);
            default:
                throw new IllegalArgumentException("Item view type " + viewType + " does not exist");
        }
    }

    @NonNull
    protected abstract RecyclerView.ViewHolder onCreateTeamViewHolder(ViewGroup parent);

    @NonNull
    protected abstract RecyclerView.ViewHolder onCreateTargetViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        @ViewType int viewType = getItemViewType(position);
        switch (viewType) {
            case RowTypeColumns.TYPE_TARGET:
                onBindTargetViewHolder(holder, position);
                break;
            case RowTypeColumns.TYPE_TEAM:
                onBindTeamViewHolder(holder, position);
                break;
            default:
                throw new IllegalArgumentException("Item view type " + viewType + " does not exist");
        }
    }

    protected abstract void onBindTeamViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract void onBindTargetViewHolder(RecyclerView.ViewHolder holder, int position);


    protected void onTargetCLick(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            long playerId = mCursor.getLong(TargetProjection.QUERY_ID);
            TargetBottomSheetDialogFragment.show(mContext, playerId);
        }
    }

    protected void onPhotoClick(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            String url = mCursor.getString(TargetProjection.QUERY_PHOTO);

            IntentUtils.startImageActivity(mContext, url);
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    protected void onHomeCLick(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(TargetProjection.QUERY_WORK);
            IntentUtils.startGeoActivity(mContext, address);
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    protected void onWorkCLick(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(TargetProjection.QUERY_WORK);
            IntentUtils.startGeoActivity(mContext, address);
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                Target.ROW_TYPE,
                Target.TEAM_NAME,
                Target.ID,
                Target.FIRST_NAME,
                Target.LAST_NAME,
                Target.ALIAS,
                Target.PHOTO,
                Target.KILL_COUNT,
                Target.JOB_CATEGORY,
                Target.HOME,
                Target.WORK,
                Target.EXTRA
        };

        int QUERY_ROW_TYPE = 0;

        int QUERY_TEAM_NAME = 1;

        int QUERY_ID = 2;

        int QUERY_FIRST_NAME = 3;

        int QUERY_LAST_NAME = 4;

        int QUERY_ALIAS = 5;

        int QUERY_PHOTO = 6;

        int QUERY_KILL_COUNT = 7;

        int QUERY_JOB_CATEGORY = 8;

        int QUERY_HOME = 9;

        int QUERY_WORK = 10;

        int QUERY_EXTRA = 11;
    }
}
