package fr.streetgames.streetwars.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.glide.CircleTransform;

public class KillerAdapter extends CursorAdapter<KillerAdapter.KillerViewHolder> {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public KillerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_killer, parent, false);
        KillerViewHolder holder = new KillerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(KillerViewHolder holder, int position) {

        if (position == 1) {
            holder.killerImageView.setMinimumWidth(2000);
        }

        Picasso
                .with(holder.itemView.getContext())
                .load("http://5.135.183.92:50220/res/davy_jones")
                .transform(new CircleTransform())
                .into(holder.killerImageView);
    }

    public class KillerViewHolder extends RecyclerView.ViewHolder {

        public final ImageView killerImageView;

        public KillerViewHolder(View itemView) {
            super(itemView);
            killerImageView = (ImageView) itemView.findViewById(R.id.killer_image);
        }

    }

    public interface KillerProjection {

        String[] PROJECTION = new String[]{
                StreetWarsContract.Target.ID,
                StreetWarsContract.Target.PHOTO
        };

        int QUERY_ID = 0;

        int QUERY_PHOTO = 1;
    }

}
