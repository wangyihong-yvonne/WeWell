package edu.neu.madcourse.wewell.ui.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Leader;
import edu.neu.madcourse.wewell.model.Reward;
import edu.neu.madcourse.wewell.service.RewardService;

public class RewardsFragment extends Fragment {

    // rewards
    private RecyclerView rewardsRecyclerView;
    private RviewAdapter rewardsRviewAdapter;
    private RecyclerView.LayoutManager rewardsRLayoutManger;

    // leaderboard
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardRviewAdapter leaderboardRviewAdapter;
    private RecyclerView.LayoutManager leaderboardRLayoutManger;

    private RewardService rewardService;

    public RewardsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);

        rewardService = new RewardService();
        init(true, false, currentUserId);
        return root;
    }

    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        rewardService.getRewardsByUser(rewardList -> {
            if (rewardList != null) {
                if (shouldCreateRecycler) {
                    createRecycler(rewardList, null);
                }
                if (shouldNotifyDataChange) {
                    rewardsRviewAdapter.notifyDataSetChanged();
                }
            }
        }, currentUserId);

        rewardService.getLeaderboard(leaderList -> {
            if (leaderList != null) {
                if (shouldCreateRecycler) {
                    createRecycler(null, leaderList);
                }
                if (shouldNotifyDataChange) {
                    leaderboardRviewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void createRecycler(List<Reward> rewardList, List<Leader> leaderList) {
        if (rewardList != null) {
            rewardsRLayoutManger = new LinearLayoutManager(getContext());
            rewardsRecyclerView = getView().findViewById(R.id.rewards_recycler);
            rewardsRecyclerView.setHasFixedSize(true);
            rewardsRviewAdapter = new RviewAdapter(rewardList);
            rewardsRecyclerView.setAdapter(rewardsRviewAdapter);
            rewardsRecyclerView.setLayoutManager(rewardsRLayoutManger);
        }

        if (leaderList != null) {
            leaderboardRLayoutManger = new LinearLayoutManager(getContext());
            leaderboardRecyclerView = getView().findViewById(R.id.leaderboard_recycler);
            leaderboardRecyclerView.setHasFixedSize(true);
            leaderboardRviewAdapter = new LeaderboardRviewAdapter(leaderList);
            leaderboardRecyclerView.setAdapter(leaderboardRviewAdapter);
            leaderboardRecyclerView.setLayoutManager(leaderboardRLayoutManger);
        }

    }

}