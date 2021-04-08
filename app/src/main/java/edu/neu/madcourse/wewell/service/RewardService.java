package edu.neu.madcourse.wewell.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.wewell.model.Reward;

public class RewardService {


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void updateReward(RefreshRewardsCallBack callback, String userId, double currentDistance) {

        db.collection("users").document(userId).collection("rewards").whereEqualTo("finished", false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Map<String, Object> unfinishedReward = (Map<String, Object>) document.getData();

                        double prevFinishedDistance = (double) unfinishedReward.get("finishedDistance");
                        double goal = (double) unfinishedReward.get("goal");
                        double curSum = prevFinishedDistance + currentDistance;
                        if (curSum >= goal) {
                            unfinishedReward.put("finished", true);
                            unfinishedReward.put("finishedDistance", goal);
                        } else {
                            unfinishedReward.put("finishedDistance", curSum);
                        }
                        db.collection("users").document(userId).collection("rewards")
                                .document(document.getId()).set(unfinishedReward);
                    }
                    callback.callBack(0);
                } else {
                    System.out.println("Error when getting rewards");
                }
            }
        });
    }

    public void getTotalDistanceByUser(TotalDistanceCallBack callBack, String userId) {
        db.collection("users").document(userId)
                .collection("totalDistance").document("total").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        double total = ((Number) documentSnapshot.get("distance")).doubleValue();
                        callBack.callBack(total);
                    }
                });
    }

    public void getRewardsByUser(CallBack callBack, String userId) {
        List<Reward> rewardList = new ArrayList<>();
        db.collection("users").document(userId).collection("rewards")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                queryDocumentSnapshots.forEach(queryDocumentSnapshot -> {
                    Map<String, Object> map = queryDocumentSnapshot.getData();
                    String title = (String) map.get("title");
                    double goal = ((Number) map.get("goal")).doubleValue();
                    double finishedDistance = ((Number) map.get("finishedDistance")).doubleValue();
                    int type = ((Number) map.get("type")).intValue();
                    Reward reward = new Reward();
                    reward.setTitle(title);
                    reward.setGoal(goal);
                    reward.setFinishedDistance(finishedDistance);
                    reward.setType(type);
                    rewardList.add(reward);
                });
            }
            callBack.callBack(rewardList);
        });
    }

    //use callback to retrieve data from firebase.
    public interface CallBack {
        void callBack(List<Reward> rewardList);
    }

    public interface TotalDistanceCallBack {
        void callBack(Double distance);
    }

    public interface RefreshRewardsCallBack {
        void callBack(int i);
    }

}
