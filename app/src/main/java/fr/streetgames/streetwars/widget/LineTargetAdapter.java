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

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.picasso.CircleTransform;

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

        holder.photoImageView.setTag(holder);
        holder.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineTargetAdapter.this.onPhotoClick(((TargetViewHolder) v.getTag()).getAdapterPosition());
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
            Picasso.with(mContext)
                    .load(mCursor.getString(TargetProjection.QUERY_PHOTO))
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_user_round_48dp)
                    .into(holder.photoImageView);
        }
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photoImageView;

        private final TextView nameTextView;

        private final TextView aliasTextView;

        public TargetViewHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.target_photo);
            nameTextView = (TextView) itemView.findViewById(R.id.target_name);
            aliasTextView = (TextView) itemView.findViewById(R.id.target_alias);
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
