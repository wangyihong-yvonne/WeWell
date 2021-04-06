package edu.neu.madcourse.wewell.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Activity;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {
    public List<Activity> activityList;
    private ItemClickListener listener;

    public RviewAdapter(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new RviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RviewHolder holder, int position) {
        Activity currentActivity = activityList.get(position);

        holder.distance_value.setText(String.valueOf(currentActivity.getDistance()));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setItemList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public ItemClickListener getListener() {
        return listener;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
}
