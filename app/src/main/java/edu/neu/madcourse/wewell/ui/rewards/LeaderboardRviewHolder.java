package edu.neu.madcourse.wewell.ui.rewards;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import edu.neu.madcourse.wewell.R;


public class LeaderboardRviewHolder extends RecyclerView.ViewHolder {

    public MaterialCardView cardView;
    public TextView title;
    public TextView description;
    public TextView ranking;

    public LeaderboardRviewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        cardView = itemView.findViewById(R.id.leader_material_card);
        title = itemView.findViewById(R.id.leader_title);
        description = itemView.findViewById(R.id.leader_description);
        ranking = itemView.findViewById(R.id.ranking);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
