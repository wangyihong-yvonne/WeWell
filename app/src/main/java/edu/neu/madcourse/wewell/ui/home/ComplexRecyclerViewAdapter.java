package edu.neu.madcourse.wewell.ui.home;

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
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.model.ActivityChart;
import edu.neu.madcourse.wewell.model.ActivitySummary;
import edu.neu.madcourse.wewell.util.Util;

//ref: https://guides.codepath.com/android/Heterogeneous-Layouts-inside-RecyclerView
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<RecyclerItem> items;

    public static final int Summary = 0, Chart = 1;

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
        if (viewType == Chart) {
            View v1 = inflater.inflate(R.layout.activity_chart_card, parent, false);
            viewHolder = new ActivityChartRviewHolder(v1);
        } else if (viewType == Summary) {
            View v2 = inflater.inflate(R.layout.activity_summary_card, parent, false);
            viewHolder = new ActivitySummaryRviewHolder(v2);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == Chart) {
            ActivityChartRviewHolder vh1 = (ActivityChartRviewHolder) viewHolder;
            configureViewHolderforCharts(vh1, position);
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

    //TODO
    private void configureViewHolderforCharts(ActivityChartRviewHolder vh, int position) {
        List<Activity> activityList = (List<Activity>) items.get(position).getObject();
        if (activityList != null) {
            AnyChartView anyChartView = vh.getAnyChartView();
            Cartesian cartesian = initColCharts(activityList);
            anyChartView.setChart(cartesian);
        }
    }

    private Cartesian initColCharts(List<Activity> activityList) {
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (Activity activity : activityList) {
            String date = Util.formatDateV2(activity.getStartTime());
            double distance = activity.getDistance();
            data.add(new ValueDataEntry(date, distance));
        }
        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(2d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);
        cartesian.yScale().maximum(20d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yAxis(0).title("Distance (KM)");
        cartesian.xScroller(true);
        cartesian.xScroller().allowRangeChange(false);
        return cartesian;
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