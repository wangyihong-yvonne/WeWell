package edu.neu.madcourse.wewell.ui.rewards;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import edu.neu.madcourse.wewell.R;


public class RviewHolder extends RecyclerView.ViewHolder {

    public MaterialCardView cardView;
    public TextView title;
    public TextView description;
    public ProgressBar progressBar;
    public ImageView imageView;
    public ImageView checkerView;

    public RviewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        cardView = itemView.findViewById(R.id.reward_material_card);
        title = itemView.findViewById(R.id.reward_title);
       // description = itemView.findViewById(R.id.reward_description);
        progressBar = itemView.findViewById(R.id.reward_progress_bar);
        imageView = itemView.findViewById(R.id.imageView10);
        checkerView = itemView.findViewById(R.id.imageView5);

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
