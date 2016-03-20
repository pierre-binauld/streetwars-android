package fr.streetgames.streetwars.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.streetgames.streetwars.R;

public class RuleAdapter extends CursorAdapter<RuleAdapter.RuleViewHolder> {

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
        if (null != mCursor && mCursor.moveToPosition(position)) {
            holder.ruleTextView.setText(mCursor.getString(0));
        }
    }

    class RuleViewHolder extends RecyclerView.ViewHolder {

        public TextView ruleTextView;

        public RuleViewHolder(View itemView) {
            super(itemView);
            ruleTextView = (TextView) itemView.findViewById(R.id.rule);
        }
    }
}
