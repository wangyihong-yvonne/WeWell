package edu.neu.madcourse.wewell.ui.home;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.Chart;

import edu.neu.madcourse.wewell.R;

public class ActivityChartRviewHolder extends RecyclerView.ViewHolder {
    private Chart chart;
    private int charType;

    public ActivityChartRviewHolder(View itemView, int type) {
        super(itemView);
        if (type == ComplexRecyclerViewAdapter.Distance_Bar_Chart) {
            chart = itemView.findViewById(R.id.barchart);
            charType = type;
        } else if (type == ComplexRecyclerViewAdapter.Calorie_Bar_Chart) {
            chart = itemView.findViewById(R.id.barchart);
            charType = type;
        }else if (type == ComplexRecyclerViewAdapter.Pace_Line_Char) {
            chart = itemView.findViewById(R.id.linechart);
            charType = type;
        }
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }
}
