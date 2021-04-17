package edu.neu.madcourse.wewell.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.model.ActivitySummary;
import edu.neu.madcourse.wewell.util.Util;

//ref: https://guides.codepath.com/android/Heterogeneous-Layouts-inside-RecyclerView
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<RecyclerItem> items;

    public static final int Summary = 0, Bar_Chart = 1, Line_Char = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(List<RecyclerItem> items) {
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }


    @Override
    public int getItemViewType(int position) {
//        if (items.get(position) instanceof ActivitySummary) {
//            return Summary;
//        } else if (items.get(position) instanceof ActivityChart) {
//            return Chart;
//        }
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Bar_Chart) {
            View v1 = inflater.inflate(R.layout.activity_bar_chart_card, parent, false);
            viewHolder = new ActivityChartRviewHolder(v1, Bar_Chart);
        } else if (viewType == Line_Char) {
            View v2 = inflater.inflate(R.layout.activity_line_chart_card, parent, false);
            viewHolder = new ActivityChartRviewHolder(v2, Line_Char);
        } else if (viewType == Summary) {
            View v3 = inflater.inflate(R.layout.activity_summary_card, parent, false);
            viewHolder = new ActivitySummaryRviewHolder(v3);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == Bar_Chart) {
            ActivityChartRviewHolder vh1 = (ActivityChartRviewHolder) viewHolder;
            configureViewHolderforCharts(vh1, position);
        } else if (viewHolder.getItemViewType() == Line_Char) {
            ActivityChartRviewHolder vh2 = (ActivityChartRviewHolder) viewHolder;
            configureViewHolderforCharts(vh2, position);
        } else if (viewHolder.getItemViewType() == Summary) {
            ActivitySummaryRviewHolder vh = (ActivitySummaryRviewHolder) viewHolder;
            configureViewHolderforSummary(vh, position);
        }
    }

    private void configureViewHolderforSummary(ActivitySummaryRviewHolder vh, int position) {
        ActivitySummary summary = (ActivitySummary) items.get(position).getObject();
        if (summary != null) {
            vh.textTotalDistance.setText(summary.getTotalDistance());
            vh.textTotalRuns.setText(String.valueOf(summary.getTotalRuns()));
            vh.textAvgPace.setText(summary.getAvgPace());
            vh.textAvgCalories.setText(summary.getAvgCalories());
        }
    }


    private void configureViewHolderforCharts(ActivityChartRviewHolder vh, int position) {
        List<Activity> activityList = (List<Activity>) items.get(position).getObject();
        if (activityList != null) {
            if (vh.getCharType() == Bar_Chart) {
                BarChart barChart = (BarChart) vh.getChart();
                initBarCharts(barChart, activityList);
            }else if (vh.getCharType() == Line_Char) {
                LineChart lineChart = (LineChart) vh.getChart();
                initLineCharts(lineChart, activityList);
            }
        }
    }


    private void initBarCharts(BarChart barChart, List<Activity> activityList) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float idx = 0;
        for (Activity activity : activityList) {
            double distance = activity.getDistance();
            barEntries.add(new BarEntry(idx, (float) distance));
            idx++;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (km)");
        barDataSet.setValueTextSize(9);
        barDataSet.setColor(Color.BLACK);
        ArrayList<String> theDates = new ArrayList<>();
        for (Activity activity : activityList) {
            String date = Util.formatDateV2(activity.getStartTime());
            theDates.add(date);
        }
        barChart.getDescription().setText("");
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getXAxis().setDrawGridLines(false);
//        barChart.setViewPortOffsets(5,10,5,0);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

//        barChart.getLegend().setEnabled(false);

        BarData theData = new BarData(barDataSet);
        theData.setBarWidth(0.5f);
        barChart.setData(theData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setVisibleXRangeMaximum(7);
        barChart.animateY(1000);
    }

    private void initLineCharts(LineChart lineChart, List<Activity> activityList) {

    }
//    private void bindDataToAdapter() {
//        // Bind adapter to recycler view object
//        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getSampleArrayList()));
//    }


//    public void setItemList(List<Object> horizontalItems) {
//        this.horizontalItems = horizontalItems;
//    }
//
//    public ItemClickListener getListener() {
//        return listener;
//    }
//
//    public void setListener(ItemClickListener listener) {
//        this.listener = listener;
//    }

}