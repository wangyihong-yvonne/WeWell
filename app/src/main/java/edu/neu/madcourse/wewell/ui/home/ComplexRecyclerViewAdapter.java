package edu.neu.madcourse.wewell.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.ActivityChart;
import edu.neu.madcourse.wewell.model.ActivitySummary;

//ref: https://guides.codepath.com/android/Heterogeneous-Layouts-inside-RecyclerView
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int Summary = 0, Chart = 1;
    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(List<Object> items) {
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ActivitySummary) {
            return Summary;
        } else if (items.get(position) instanceof ActivityChart) {
            return Chart;
        }
        return -1;
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
        ActivitySummary summary = (ActivitySummary) items.get(position);
        if (summary != null) {
            vh.textTotalDistance.setText(summary.getTotalDistance());
            vh.textTotalRuns.setText(String.valueOf(summary.getTotalRuns()));
            vh.textAvgPace.setText(summary.getAvgPace());
            vh.textAvgCalories.setText(summary.getAvgCalories());
        }
    }

    //TODO
    private void configureViewHolderforCharts(ActivityChartRviewHolder vh, int position) {
        ActivityChart chart = (ActivityChart) items.get(position);
        if (chart != null) {
//            vh.setAnyChartView()
        }
    }

//    private void bindDataToAdapter() {
//        // Bind adapter to recycler view object
//        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getSampleArrayList()));
//    }


    public List<Object> getHorizontalItemsList() {
        return items;
    }

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