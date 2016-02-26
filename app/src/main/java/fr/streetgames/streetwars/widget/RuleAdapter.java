package fr.streetgames.streetwars.widget;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.streetgames.streetwars.R;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    private Cursor mCursor;

    @Override
    public RuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.row_rule,
                        parent,
                        false
                );
        return new RuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RuleViewHolder holder, int position) {
//        if (null != mCursor && mCursor.moveToPosition(position)) {
//            holder.ruleTextView.setText(mCursor.getString(0));
//        }
        Resources res = holder.ruleTextView.getResources();
        switch (position) {
            default:
            case 0:
                holder.ruleTextView.setText(res.getString(R.string.water_code_rule_1));
                break;
            case 1:
                holder.ruleTextView.setText(res.getString(R.string.water_code_rule_2));
                break;
            case 2:
                holder.ruleTextView.setText(res.getString(R.string.water_code_rule_3));
                break;
            case 3:
                holder.ruleTextView.setText(res.getString(R.string.water_code_rule_4));
                break;
        }
    }

    @Override
    public int getItemCount() {
//        if(null != mCursor) {
//            return mCursor.getCount();
//        }
        return 4;
    }

    public void swapCursor(@Nullable Cursor cursor) {
        mCursor = cursor;
    }

    class RuleViewHolder extends RecyclerView.ViewHolder {

        public TextView ruleTextView;

        public RuleViewHolder(View itemView) {
            super(itemView);
            ruleTextView = (TextView) itemView.findViewById(R.id.rule);
        }
    }
}
