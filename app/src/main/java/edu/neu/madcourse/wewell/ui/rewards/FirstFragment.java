package edu.neu.madcourse.wewell.ui.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    TextView TextViewR1;
    TextView TextViewR2;
    TextView TextViewR3;
    public List<Leader> leaderboard;

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

        TextViewR1 = root.findViewById(R.id.textViewR1);
        TextViewR2 = root.findViewById(R.id.textViewR2);
        TextViewR3 = root.findViewById(R.id.textViewR3);

        init(true, false, currentUserId);
        return root;
    }

    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        rewardService.getLeaderboard(leaderList -> {
            if (leaderList != null) {
                int size = leaderList.size();
                if (size > 0) {
                    TextViewR1.setText(leaderList.get(0).getName());
                    TextViewR1.setBackgroundResource(R.drawable.ic_group_35);
                }
                if (size > 1) {
                    TextViewR2.setText(leaderList.get(1).getName());
                    TextViewR2.setBackgroundResource(R.drawable.ic_group_38);
                }
                if (size > 2) {
                    TextViewR3.setText(leaderList.get(2).getName());
                    TextViewR3.setBackgroundResource(R.drawable.ic_group_37);
                }
                if (shouldCreateRecycler) {
                    if (leaderList.size() > 3) {
                        createRecycler(leaderList.subList(3, size));
                    }
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
