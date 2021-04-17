package edu.neu.madcourse.wewell.ui.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Reward;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {
    public List<Reward> rewardList;
    private ItemClickListener listener;

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
        if (currentReward.getType() == 0) {
            holder.description.setText(formatAmount + " kilometers left");
        } else {
            holder.description.setText(formatAmount + " calories left");
        }
        int progress = (int)(currentReward.getFinishedAmount() * 100 / currentReward.getGoal());
        holder.progressBar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }


}
