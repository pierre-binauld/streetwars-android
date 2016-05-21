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

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsJobCategory;

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
                CardTargetAdapter.this.onHomeCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        holder.workImageButton.setTag(holder);
        holder.workImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardTargetAdapter.this.onWorkCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
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
            holder.nameTextView.setText(
                    mResources.getString(
                            R.string.util_name,
                            mCursor.getString(TargetProjection.QUERY_FIRST_NAME),
                            mCursor.getString(TargetProjection.QUERY_LAST_NAME)
                    )
            );
            holder.aliasTextView.setText(mCursor.getString(TargetProjection.QUERY_ALIAS));
            holder.extraTextView.setText(mCursor.getString(TargetProjection.QUERY_EXTRA));
            Picasso.with(mContext)
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .placeholder(R.drawable.placeholder_user_full_336dp)
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

        private final TextView nameTextView;

        private final TextView aliasTextView;

        private final TextView extraTextView;

        private final ImageButton homeImageButton;

        private final ImageButton workImageButton;

        public TargetViewHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.target_photo);
            nameTextView = (TextView) itemView.findViewById(R.id.target_name);
            aliasTextView = (TextView) itemView.findViewById(R.id.target_alias);
            extraTextView = (TextView) itemView.findViewById(R.id.target_extra);
            homeImageButton = (ImageButton) itemView.findViewById(R.id.target_ic_home);
            workImageButton = (ImageButton) itemView.findViewById(R.id.target_ic_work);
        }
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;

        public TeamViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }

}
