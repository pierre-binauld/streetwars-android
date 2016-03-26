package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
import fr.streetgames.streetwars.utils.IntentUtils;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.*;

public class TargetAdapter extends CursorAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TargetAdapter";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Target.TYPE_TEAM,
            Target.TYPE_TARGET
    })
    public @interface ViewType {}

    @NonNull
    private Context mContext;

    @NonNull
    private Resources mRes;

    public TargetAdapter(@NonNull Context context) {
        mContext = context;
        mRes = mContext.getResources();
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

    private RecyclerView.ViewHolder onCreateTeamViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team, parent, false);
        TeamViewHolder holder = new TeamViewHolder(view);
        return holder;
    }

    @NonNull
    private RecyclerView.ViewHolder onCreateTargetViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_target, parent, false);
        TargetViewHolder holder = new TargetViewHolder(view);

        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetAdapter.this.onTargetCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        holder.homeImageButton.setTag(holder);
        holder.homeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetAdapter.this.onHomeCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        holder.workImageButton.setTag(holder);
        holder.workImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetAdapter.this.onWorkCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        @ViewType int viewType = getItemViewType(position);
        switch (viewType) {
            case fr.streetgames.streetwars.database.TypeColumns.TYPE_TARGET:
                onBindTargetViewHolder((TargetViewHolder) holder, position);
                break;
            case fr.streetgames.streetwars.database.TypeColumns.TYPE_TEAM:
                onBindTeamViewHolder((TeamViewHolder) holder, position);
                break;
            default:
                throw new IllegalArgumentException("Item view type " + viewType + " does not exist");
        }
    }

    private void onBindTeamViewHolder(TeamViewHolder holder, int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            holder.nameTextView.setText(mCursor.getString(TargetProjection.QUERY_TEAM_NAME));
        }
    }

    private void onBindTargetViewHolder(TargetViewHolder holder, int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.mameTextView.setText(
                    mRes.getString(
                            R.string.util_name,
                            mCursor.getString(TargetProjection.QUERY_FIRST_NAME),
                            mCursor.getString(TargetProjection.QUERY_LAST_NAME)
                    )
            );
            holder.aliasTextView.setText(mCursor.getString(TargetProjection.QUERY_ALIAS));
            holder.extraTextView.setText(mCursor.getString(TargetProjection.QUERY_EXTRA));
            Glide.with(mContext)
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .placeholder(R.color.colorAccent)
                    .into(holder.photoImageView);

            @StreetWarsJobCategory.JobCategory int jobCat = mCursor.getInt(TargetProjection.QUERY_JOB_CATEGORY);
            switch (jobCat) {
                case StreetWarsJobCategory.NO_jOB:
                    holder.homeImageButton.setImageResource(R.drawable.ic_no_job_accent_24dp);
                    holder.workImageButton.setVisibility(View.GONE);
                    break;
                case StreetWarsJobCategory.STUDENT:
                    holder.homeImageButton.setImageResource(R.drawable.ic_home_accent_24dp);
                    holder.workImageButton.setImageResource(R.drawable.ic_school_accent_24dp);
                    holder.workImageButton.setVisibility(View.VISIBLE);
                    break;
                case StreetWarsJobCategory.WORKER:
                    holder.homeImageButton.setImageResource(R.drawable.ic_home_accent_24dp);
                    holder.workImageButton.setImageResource(R.drawable.ic_work_accent_24dp);
                    holder.workImageButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void onTargetCLick(int position) {

    }

    private void onHomeCLick(int position) {
        if(null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(TargetProjection.QUERY_HOME);
            boolean started = IntentUtils.startGeoActivity(mContext, address);
            if (!started) {
                //TODO Snackbar: No app found to open address
            }
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    private void onWorkCLick(int position) {
        if(null != mCursor && mCursor.moveToPosition(position)) {
            String address = mCursor.getString(TargetProjection.QUERY_WORK);
            boolean started = IntentUtils.startGeoActivity(mContext, address);
            if (!started) {
                //TODO Snackbar: No app found to open address
            }
        }
        else {
            Log.d(TAG, "onHomeCLick: Position #" + position + " is not reachable.");
        }
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photoImageView;

        private final TextView mameTextView;

        private final TextView aliasTextView;

        private final TextView extraTextView;

        private final ImageButton homeImageButton;

        private final ImageButton workImageButton;

        public TargetViewHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.photo);
            mameTextView = (TextView) itemView.findViewById(R.id.name);
            aliasTextView = (TextView) itemView.findViewById(R.id.alias);
            extraTextView = (TextView) itemView.findViewById(R.id.extra);
            homeImageButton = (ImageButton) itemView.findViewById(R.id.ic_home);
            workImageButton = (ImageButton) itemView.findViewById(R.id.ic_work);
        }
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;

        public TeamViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
                Target.TYPE,
                Target.TEAM_NAME,
                Target.ID,
                Target.FIRST_NAME,
                Target.LAST_NAME,
                Target.ALIAS,
                Target.PHOTO,
                Target.JOB_CATEGORY,
                Target.HOME,
                Target.WORK,
                Target.EXTRA
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
