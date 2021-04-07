package edu.neu.madcourse.wewell.ui.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Reward;
import edu.neu.madcourse.wewell.service.RewardService;

public class RewardsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
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
                    createRecycler(rewardList);
                }
                if (shouldNotifyDataChange) {
                    rviewAdapter.notifyDataSetChanged();
                }
            }
        }, currentUserId);

    }

    private void createRecycler(List<Reward> rewardList) {
        rLayoutManger = new LinearLayoutManager(getContext());
        recyclerView = getView().findViewById(R.id.rewards_recycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new RviewAdapter(rewardList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

}