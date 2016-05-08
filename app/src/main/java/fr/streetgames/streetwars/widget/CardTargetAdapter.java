package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;

public class CardTargetAdapter extends TargetAdapter {

    private static final String TAG = "CardTargetAdapter";

    public CardTargetAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateTeamViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team_card, parent, false);
        TeamViewHolder holder = new TeamViewHolder(view);
        return holder;
    }

    @Override
    @NonNull
    protected RecyclerView.ViewHolder onCreateTargetViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_target_card, parent, false);
        TargetViewHolder holder = new TargetViewHolder(view);

        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardTargetAdapter.this.onTargetCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        holder.homeImageButton.setTag(holder);
        holder.homeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardTargetAdapter.this.onHomeCLick(((TargetViewHolder) v.getTag()).getAdapterPosition(), TargetProjection.QUERY_HOME);
            }
        });

        holder.workImageButton.setTag(holder);
        holder.workImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardTargetAdapter.this.onWorkCLick(((TargetViewHolder) v.getTag()).getAdapterPosition(), TargetProjection.QUERY_WORK);
            }
        });

        return holder;
    }

    @Override
    protected void onBindTeamViewHolder(RecyclerView.ViewHolder recyclerViewHolder, int position) {
        TeamViewHolder holder = (TeamViewHolder) recyclerViewHolder;
        if (null != mCursor && mCursor.moveToPosition(position)) {
            holder.nameTextView.setText(mCursor.getString(TargetProjection.QUERY_TEAM_NAME));
            //TODO if orientation == horizontal
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
    }

    @Override
    protected void onBindTargetViewHolder(RecyclerView.ViewHolder recyclerViewHolder, int position) {
        TargetViewHolder holder = (TargetViewHolder) recyclerViewHolder;
        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.mameTextView.setText(
                    mResources.getString(
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


    public interface TargetProjection extends TargetAdapter.TargetProjection {

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