package edu.neu.madcourse.wewell.ui.home;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.wewell.R;


public class ActivitySummaryRviewHolder extends RecyclerView.ViewHolder {
    public TextView textTotalDistance = null;
    public TextView textTotalRuns = null;
    public TextView textAvgPace = null;
    public TextView textAvgCalories = null;

    public ActivitySummaryRviewHolder(View itemView) {
        super(itemView);
        textTotalDistance = itemView.findViewById(R.id.et_total_dis);
        textTotalRuns = itemView.findViewById(R.id.et_total_runs);
        textAvgCalories = itemView.findViewById(R.id.et_avg_calorie);
        textAvgPace = itemView.findViewById(R.id.et_avg_pace);
    }

}
