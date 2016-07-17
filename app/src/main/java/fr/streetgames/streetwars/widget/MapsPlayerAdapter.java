package fr.streetgames.streetwars.widget;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.api.StreetWarsPlayer;
import fr.streetgames.streetwars.database.PlayerColumns;
import fr.streetgames.streetwars.picasso.CircleTransform;

public class MapsPlayerAdapter extends RecyclerView.Adapter<MapsPlayerAdapter.PlayerViewHolder> {

    @Nullable
    private Cursor mCursor;

    @NonNull
    private CircleTransform mCircleTransform;

    public MapsPlayerAdapter() {
        mCircleTransform = new CircleTransform();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.row_player_avatar,
                        parent,
                        false
                );
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        if (null != mCursor && mCursor.moveToPosition(position)) {
            holder.playerId = mCursor.getLong(PlayerProjection.QUERY_ID);
            Picasso.with(holder.itemView.getContext())
                    .load(mCursor.getString(PlayerProjection.QUERY_PHOTO))
                    .transform(mCircleTransform)
                    .placeholder(R.drawable.placeholder_user_round_48dp)
                    .into(holder.playerPhotoImageView);
        }
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        public final ImageView playerPhotoImageView;

        public long playerId = StreetWarsPlayer.NO_ID;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            playerPhotoImageView = (ImageView) itemView.findViewById(R.id.player_avatar);
        }

    }

    public interface PlayerProjection {
        String[] PROJECTION = new String[] {
                PlayerColumns.ID,
                PlayerColumns.PHOTO
        };

        int QUERY_ID = 0;

        int QUERY_PHOTO = 1;
    }
}
