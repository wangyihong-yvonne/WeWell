package edu.neu.madcourse.wewell.service;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.wewell.model.Reward;

public class RewardService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void updateReward(String userId, double currentDistance) {

        // get default goals
        DocumentReference documentReference = db.collection("DefaultGoals").document(userId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // todo
            }
        });
    }

    public void getRewardsByUser(CallBack callBack, String userId) {
        DocumentReference documentReference = db.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // todo
                List<Map<String, Object>> rewardsFromDB = (List<Map<String, Object>>) documentSnapshot.get("rewards");
                List<Reward> rewardList = new ArrayList<>();
                if (rewardsFromDB != null) {
                    for (Map<String, Object> map : rewardsFromDB) {
                        String title = (String) map.get("title");
                        long goal = (long) map.get("goal");
                        long finishedDistance = (long) map.get("finishedDistance");
                        int type = ((Number) map.get("type")).intValue();
                        Reward reward = new Reward();
                        reward.setTitle(title);
                        reward.setGoal(goal);
                        reward.setFinishedDistance(finishedDistance);
                        reward.setType(type);
                        rewardList.add(reward);
                    }
                }


                System.out.println(rewardList);
                callBack.callBack(rewardList);
            }
        });
    }

    //use callback to retrieve data from firebase.
    public interface CallBack {
        void callBack(List<Reward> activityList);

    }

}
