package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.streetgames.streetwars.utils.IntentUtils;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Target;

public abstract class TargetAdapter extends CursorAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TargetAdapter";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Target.TYPE_TEAM,
            Target.TYPE_TARGET
    })
    public @interface ViewType {}

    @NonNull
    protected Context mContext;

    @NonNull
    protected Resources mResources;

    public TargetAdapter(@NonNull Context context) {
        mContext = context;
        mResources = mContext.getResources();
    }

    @CallSuper
    @Override
    public int getItemViewType(int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            return mCursor.getInt(TargetProjection.QUERY_TYPE) == Target.TYPE_TEAM
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
            case fr.streetgames.streetwars.database.TypeColumns.TYPE_TARGET:
                onBindTargetViewHolder(holder, position);
                break;
            case fr.streetgames.streetwars.database.TypeColumns.TYPE_TEAM:
                onBindTeamViewHolder(holder, position);
                break;
            default:
                throw new IllegalArgumentException("Item view type " + viewType + " does not exist");
        }
    }

    protected abstract void onBindTeamViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract void onBindTargetViewHolder(RecyclerView.ViewHolder holder, int position);

    protected void onTargetCLick(int position) {

    }

    protected void onHomeCLick(int position, int homeColumnIndex) {
        if(null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(homeColumnIndex);
            boolean started = IntentUtils.startGeoActivity(mContext, address);
            if (!started) {
                //TODO Snackbar: No app found to open address
            }
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    protected void onWorkCLick(int position, int workColumnIndex) {
        if(null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(workColumnIndex);
            boolean started = IntentUtils.startGeoActivity(mContext, address);
            if (!started) {
                //TODO Snackbar: No app found to open address
            }
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    public interface TargetProjection {

        int QUERY_TYPE = 0;
    }
}
