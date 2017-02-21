package fr.streetgames.streetwars.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsTeamMate;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.picasso.CircleTransform;

public class TeamMateAdapter extends CursorAdapter<TeamMateAdapter.TeamMateViewHolder> {

    @Override
    public TeamMateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team_mate, parent, false);
        TeamMateViewHolder holder = new TeamMateViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TeamMateViewHolder holder, int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.teamMateId = mCursor.getLong(TeamMateProjection.QUERY_ID);
            Picasso
                    .with(holder.itemView.getContext())
                    .load(mCursor.getString(TeamMateProjection.QUERY_PHOTO))
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_user_round_48dp)
                    .into(holder.teamMatePhotoImageView);
        }
    }

    public class TeamMateViewHolder extends RecyclerView.ViewHolder {

        public final ImageView teamMatePhotoImageView;

        public long teamMateId = StreetWarsTeamMate.NO_ID;

        public TeamMateViewHolder(View itemView) {
            super(itemView);
            teamMatePhotoImageView = (ImageView) itemView.findViewById(R.id.killer_image);
        }

    }

    public interface TeamMateProjection {

        String[] PROJECTION = new String[]{
                StreetWarsContract.Player.ID,
                StreetWarsContract.Player.PHOTO
        };

        int QUERY_ID = 0;

        int QUERY_PHOTO = 1;
    }

}
