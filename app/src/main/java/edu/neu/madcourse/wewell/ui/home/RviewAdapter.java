package edu.neu.madcourse.wewell.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.util.Util;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {
    public List<Activity> activityList;
    private ItemClickListener listener;
    private Context context;

    public RviewAdapter(List<Activity> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        return new RviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RviewHolder holder, int position) {
        Activity currentActivity = activityList.get(position);
        holder.distance_value.setText(currentActivity.getDistance() + " KM");
        holder.calorie_value.setText(String.valueOf(currentActivity.getCalories()));
        holder.pace_value.setText(Util.formatTime(currentActivity.getPace()));
        holder.start_time_value.setText(Util.formatDate((currentActivity.getStartTime())));
        holder.time_value.setText(Util.formatTime(currentActivity.getRunningTime()));

        holder.imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey my friend,"+"\nToday I have completed " + currentActivity.getDistance()+" KM at pace "+currentActivity.getPace()+"🏃‍♀️🏃‍♂️"+"\nWow! I have burnt "+ currentActivity.getCalories()+" calories💪🥳"+"\nCome to join me!"+" app download link"
);
                intent.setType("text/plain");
                                context.startActivity(Intent.createChooser(intent,"Share to "));
            }
        });
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
