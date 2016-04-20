package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;

public class LineTargetAdapter extends TargetAdapter {

    public LineTargetAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateTeamViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team_line, parent, false);
        TeamViewHolder holder = new TeamViewHolder(view);
        return holder;
    }

    @Override
    @NonNull
    protected RecyclerView.ViewHolder onCreateTargetViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_target_line, parent, false);
        TargetViewHolder holder = new TargetViewHolder(view);

        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineTargetAdapter.this.onTargetCLick(((TargetViewHolder) v.getTag()).getAdapterPosition());
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
            Glide.with(mContext)
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .placeholder(R.color.colorAccent)
                    .into(holder.photoImageView);
        }
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photoImageView;

        private final TextView mameTextView;

        private final TextView aliasTextView;

        public TargetViewHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.photo);
            mameTextView = (TextView) itemView.findViewById(R.id.name);
            aliasTextView = (TextView) itemView.findViewById(R.id.alias);
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
        };

        int QUERY_TEAM_NAME = 1;

        int QUERY_ID = 2;

        int QUERY_FIRST_NAME = 3;

        int QUERY_LAST_NAME = 4;

        int QUERY_ALIAS = 5;

        int QUERY_PHOTO = 6;
    }
}
