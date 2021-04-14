package edu.neu.madcourse.wewell.ui.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Leader;

public class LeaderboardRviewAdapter extends RecyclerView.Adapter<LeaderboardRviewHolder> {
    public List<Leader> leaderboard;
    private ItemClickListener listener;

    public LeaderboardRviewAdapter(List<Leader> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public LeaderboardRviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_card, parent, false);
        return new LeaderboardRviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(LeaderboardRviewHolder holder, int position) {
        Leader leader = leaderboard.get(position);

        holder.title.setText(String.valueOf(leader.getName()));
        String formatDistance = String.format("%.2f", leader.getDistance());
        holder.description.setText(formatDistance + " km");
        holder.ranking.setText(String.valueOf(leader.getRanking() + 1));
    }

    @Override
    public int getItemCount() {
        return leaderboard.size();
    }


}
