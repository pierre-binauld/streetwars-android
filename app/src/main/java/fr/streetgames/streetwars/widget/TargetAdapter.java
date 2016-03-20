package fr.streetgames.streetwars.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public class TargetAdapter extends CursorAdapter<TargetAdapter.TargetViewHolder> {

    @NonNull
    private Context mContext;

    @NonNull
    private Resources mRes;

    public TargetAdapter(@NonNull Context context) {
        mContext = context;
        mRes = mContext.getResources();
    }

    @Override
    public TargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contract, parent, false);
        TargetViewHolder holder = new TargetViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TargetViewHolder holder, int position) {
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

    public interface TargetProjection {

        String[] PROJECTION = new String[]{
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

        int QUERY_ID = 0;

        int QUERY_FIRST_NAME = 1;

        int QUERY_LAST_NAME = 2;

        int QUERY_ALIAS = 3;

        int QUERY_PHOTO = 4;

        int QUERY_JOB_CATEGORY = 5;

        int QUERY_HOME = 6;

        int QUERY_WORK = 7;

        int QUERY_EXTRA = 8;
    }
}
