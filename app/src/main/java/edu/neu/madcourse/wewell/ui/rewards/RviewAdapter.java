package edu.neu.madcourse.wewell.ui.rewards;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Reward;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {
    public List<Reward> rewardList;
    private ItemClickListener listener;

    int[] images ={R.drawable.ic_badge1, R.drawable.ic_badge2, R.drawable.ic_badge3, R.drawable.ic_badge4, R.drawable.ic_badge5, R.drawable.ic_badge6, R.drawable.ic_burn, R.drawable.ic_calories, R.drawable.ic_check};

    public RviewAdapter(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_card, parent, false);
        return new RviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RviewHolder holder, int position) {
        Reward currentReward = rewardList.get(position);
        holder.title.setText(String.valueOf(currentReward.getTitle()));
        double left = currentReward.getGoal() - currentReward.getFinishedAmount();

        String formatAmount = String.format("%.2f", left);
//        if (currentReward.getType() == 0) {
//            holder.description.setText(formatAmount + " kilometers left");
//        } else {
//            holder.description.setText(formatAmount + " calories left");
//        }
        int progress = (int)(currentReward.getFinishedAmount() * 100 / currentReward.getGoal());
        holder.progressBar.setProgress(progress);
       

        if (currentReward.getTitle().equals( "First run!")){
            holder.imageView.setImageResource(images[0]);
           holder.imageView5.setVisibility(View.VISIBLE);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("1KM Milestone")){
            holder.imageView.setImageResource(images[1]);
            holder.imageView5.setVisibility(View.VISIBLE);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("10KM Milestone")){
            holder.imageView.setImageResource(images[2]);
            holder.imageView5.setVisibility(View.VISIBLE);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("20KM Milestone")){
            holder.imageView5.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(images[3]);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("30KM Milestone")){
            holder.imageView5.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(images[4]);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("50KM Milestone")){
            holder.imageView5.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(images[5]);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("Consume 100cal in one run")){
            holder.imageView5.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(images[6]);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
        if (currentReward.getTitle().equals("Consume 200cal in one run")){
            holder.imageView5.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(images[7]);
            if(left > 0){
                setBW(holder.imageView);
                holder.imageView5.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void setBW(ImageView iv){

        float brightness = 10; // change values to suite your need

        float[] colorMatrix = {
                0.33f, 0.33f, 0.33f, 0, brightness,
                0.33f, 0.33f, 0.33f, 0, brightness,
                0.33f, 0.33f, 0.33f, 0, brightness,
                0, 0, 0, 1, 0
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        iv.setColorFilter(colorFilter);
    }


    @Override
    public int getItemCount() {
        return rewardList.size();
    }


}
