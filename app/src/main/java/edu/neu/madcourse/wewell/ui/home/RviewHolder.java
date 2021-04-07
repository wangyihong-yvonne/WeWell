package edu.neu.madcourse.wewell.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.wewell.R;


public class RviewHolder extends RecyclerView.ViewHolder {
    public TextView distance_value;
    public TextView start_time_value;
    public TextView pace_value;
    public TextView calorie_value;
    public TextView time_value;

    public RviewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        distance_value = itemView.findViewById(R.id.distance_val);
        start_time_value = itemView.findViewById(R.id.start_time_val);
        pace_value = itemView.findViewById(R.id.pace_val);
        calorie_value = itemView.findViewById(R.id.calorie_val);
        time_value = itemView.findViewById(R.id.time_val);

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
