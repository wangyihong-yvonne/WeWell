package edu.neu.madcourse.wewell.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        holder.distance_value.setText(String.valueOf(currentActivity.getDistance()) + " KM");
        holder.calorie_value.setText(String.valueOf(currentActivity.getCalories()));
        long pace = currentActivity.getPace();
        holder.pace_value.setText(formatTime(pace));
        long startTime = currentActivity.getStartTime();
        Date date = new Date(startTime);
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.start_time_value.setText(format.format(date));
        long runningTime = currentActivity.getRunningTime();
        holder.time_value.setText(formatTime(runningTime));
    }

    private String formatTime(long time) {
        long minutes = (time / 1000) / 60;
        int seconds = (int) ((time / 1000) % 60);
        String formattedTime = minutes + "'" + seconds + "''";
        return formattedTime;
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
