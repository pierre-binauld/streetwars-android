package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
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

    protected TargetBottomSheetViewHolder onCreateTargetBottomSheetViewHolder(BottomSheetDialog bottomSheetDialog) {
        return new TargetBottomSheetViewHolder(bottomSheetDialog);
    }

    public void onBindTargetBottomSheet(@NonNull TargetBottomSheetViewHolder holder, int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            holder.nameTextView.setText(
                    mResources.getString(
                            R.string.util_name,
                            mCursor.getString(TargetProjection.QUERY_FIRST_NAME),
                            mCursor.getString(TargetProjection.QUERY_LAST_NAME)
                    )
            );
            holder.aliasTextView.setText(mCursor.getString(TargetProjection.QUERY_ALIAS));
            holder.extraTextView.setText(mCursor.getString(TargetProjection.QUERY_EXTRA));
            holder.homeTextView.setText(mCursor.getString(TargetProjection.QUERY_HOME));
            holder.workTextView.setText(mCursor.getString(TargetProjection.QUERY_WORK));
            Picasso.with(mContext)
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .placeholder(R.color.colorAccent)
                    .into(holder.photoImageButton);

            @StreetWarsJobCategory.JobCategory int jobCat = mCursor.getInt(TargetProjection.QUERY_JOB_CATEGORY);
            switch (jobCat) {
                case StreetWarsJobCategory.NO_jOB:
                    holder.homeView.setVisibility(View.VISIBLE);
                    holder.workView.setVisibility(View.GONE);
                    holder.homeImageView.setImageResource(R.drawable.ic_no_job_accent_24dp);
                    holder.homeLabelTextView.setText(R.string.target_home);
                    break;
                case StreetWarsJobCategory.STUDENT:
                    holder.homeView.setVisibility(View.VISIBLE);
                    holder.workView.setVisibility(View.VISIBLE);
                    holder.homeImageView.setImageResource(R.drawable.ic_home_accent_24dp);
                    holder.workImageView.setImageResource(R.drawable.ic_school_accent_24dp);
                    holder.homeLabelTextView.setText(R.string.target_home);
                    holder.workLabelTextView.setText(R.string.target_school);
                    break;
                case StreetWarsJobCategory.WORKER:
                    holder.homeView.setVisibility(View.VISIBLE);
                    holder.workView.setVisibility(View.VISIBLE);
                    holder.homeImageView.setImageResource(R.drawable.ic_home_accent_24dp);
                    holder.workImageView.setImageResource(R.drawable.ic_work_accent_24dp);
                    holder.homeLabelTextView.setText(R.string.target_home);
                    holder.workLabelTextView.setText(R.string.target_work);
                    break;
            }
        }
    }

    protected void onTargetCLick(int position) {
        mBottomSheetDialog = new BottomSheetDialog(mContext);
        mBottomSheetDialog.setContentView(R.layout.sheet_target);
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

        TargetBottomSheetViewHolder targetBottomSheetViewHolder = onCreateTargetBottomSheetViewHolder(mBottomSheetDialog);

        onBindTargetBottomSheet(targetBottomSheetViewHolder, position);

        mBottomSheetDialog.show();
    }

    protected void onHomeCLick(int position, int homeColumnIndex) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
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
        if (null != mCursor && mCursor.moveToPosition(position)) {
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

    public class TargetBottomSheetViewHolder {

        public final View homeView;

        public final View workView;

        public final ImageButton photoImageButton;

        public final ImageView homeImageView;

        public final ImageView workImageView;

        public final TextView nameTextView;

        public final TextView aliasTextView;

        public final TextView extraTextView;

        public final TextView homeTextView;

        public final TextView workTextView;

        public final TextView homeLabelTextView;

        public final TextView workLabelTextView;

        public TargetBottomSheetViewHolder(BottomSheetDialog bottomSheetDialog) {
            homeView = bottomSheetDialog.findViewById(R.id.target_container_home);
            workView = bottomSheetDialog.findViewById(R.id.target_container_work);
            photoImageButton = (ImageButton) bottomSheetDialog.findViewById(R.id.target_photo);
            homeImageView = (ImageView) bottomSheetDialog.findViewById(R.id.target_ic_home);
            workImageView = (ImageView) bottomSheetDialog.findViewById(R.id.target_ic_work);
            nameTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_name);
            aliasTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_alias);
            extraTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_extra);
            homeTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_home);
            workTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_work);
            homeLabelTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_label_home);
            workLabelTextView = (TextView) bottomSheetDialog.findViewById(R.id.target_label_work);
        }

    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                StreetWarsContract.Target.TYPE,
                StreetWarsContract.Target.TEAM_NAME,
                StreetWarsContract.Target.ID,
                StreetWarsContract.Target.FIRST_NAME,
                StreetWarsContract.Target.LAST_NAME,
                StreetWarsContract.Target.ALIAS,
                StreetWarsContract.Target.PHOTO,
                StreetWarsContract.Target.JOB_CATEGORY,
                StreetWarsContract.Target.HOME,
                StreetWarsContract.Target.WORK,
                StreetWarsContract.Target.EXTRA
        };

        int QUERY_TYPE = 0;

        int QUERY_TEAM_NAME = 1;

        int QUERY_ID = 2;

        int QUERY_FIRST_NAME = 3;

        int QUERY_LAST_NAME = 4;

        int QUERY_ALIAS = 5;

        int QUERY_PHOTO = 6;

        int QUERY_JOB_CATEGORY = 7;

        int QUERY_HOME = 8;

        int QUERY_WORK = 9;

        int QUERY_EXTRA = 10;
    }
}
