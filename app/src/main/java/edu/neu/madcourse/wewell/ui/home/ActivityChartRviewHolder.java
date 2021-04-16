package edu.neu.madcourse.wewell.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;

import edu.neu.madcourse.wewell.R;


public class ActivityChartRviewHolder extends RecyclerView.ViewHolder {
    AnyChartView anyChartView;

    public ActivityChartRviewHolder(View itemView) {
        super(itemView);
        anyChartView = itemView.findViewById(R.id.any_chart_view);
    }

    public AnyChartView getAnyChartView() {
        return anyChartView;
    }

        public void setAnyChartView(AnyChartView anyChartView) {
        this.anyChartView = anyChartView;
    }

}
