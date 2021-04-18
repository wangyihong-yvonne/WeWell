package edu.neu.madcourse.wewell.ui.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Reward;
import edu.neu.madcourse.wewell.service.RewardService;

public class SecondFragment extends Fragment {
    private RewardService rewardService;
    // rewards
    private RecyclerView rewardsRecyclerView;
    private RviewAdapter rewardsRviewAdapter;
    private RecyclerView.LayoutManager rewardsRLayoutManger;
    //badge
    private ImageView imgView;
    private ImageView imgView1;
    private ImageView imgView2;

    int[] images ={R.drawable.onekk, R.drawable.twokk, R.drawable.threekk};

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View root = inflater.inflate(R.layout.reward_second, container, false);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);

//            imgView=root.findViewById(R.id.imageView5);
//            imgView1=root.findViewById(R.id.imageView8);
//            imgView2=root.findViewById(R.id.imageView9);
            rewardService = new RewardService();
            init(true, false, currentUserId);
            return root;
        }
    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
//        rewardService.getTotalDistanceByUser(distance -> {
//            String formatDistance = String.format("%.2f", distance);
//            Double TotalD = Double.valueOf(formatDistance);
//            if (TotalD >= 1){
//                imgView.setImageResource(images[0]);
//            }
//            if (TotalD >= 2){
//                imgView1.setImageResource(images[1]);
//            }
//            if (TotalD >= 3){
//                imgView2.setImageResource(images[2]);
//           }
//        }, currentUserId);
        rewardService.getRewardsByUser(rewardList -> {
            if (rewardList != null) {
                if (shouldCreateRecycler) {
                    createRecycler(rewardList);
                }
                if (shouldNotifyDataChange) {
                    rewardsRviewAdapter.notifyDataSetChanged();
                }
            }
        }, currentUserId);
    }

    private void createRecycler(List<Reward> rewardList) {
        if (rewardList != null) {
            rewardsRLayoutManger = new LinearLayoutManager(getContext());
            rewardsRecyclerView = getView().findViewById(R.id.rewards_recycler);
            rewardsRecyclerView.setHasFixedSize(true);
            rewardsRviewAdapter = new RviewAdapter(rewardList);
            rewardsRecyclerView.setAdapter(rewardsRviewAdapter);
            rewardsRecyclerView.setLayoutManager(rewardsRLayoutManger);
        }

    }
}