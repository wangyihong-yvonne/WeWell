package edu.neu.madcourse.wewell.ui.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Leader;
import edu.neu.madcourse.wewell.service.RewardService;

public class FirstFragment extends Fragment {
    // leaderboard
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardRviewAdapter leaderboardRviewAdapter;
    private RecyclerView.LayoutManager leaderboardRLayoutManger;
    private RewardService rewardService;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.reward_first, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);
        rewardService = new RewardService();
        init(true, false, currentUserId);
        return root;
    }

    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        rewardService.getLeaderboard(leaderList -> {
            if (leaderList != null) {
                if (shouldCreateRecycler) {
                    createRecycler(leaderList);
                }
                if (shouldNotifyDataChange) {
                    leaderboardRviewAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void createRecycler (List<Leader> leaderList) {
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
