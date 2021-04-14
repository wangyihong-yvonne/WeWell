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
import java.util.Optional;

import edu.neu.madcourse.wewell.model.Leader;
import edu.neu.madcourse.wewell.model.Reward;
import edu.neu.madcourse.wewell.model.User;

public class RewardService {


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void updateReward(RefreshRankingCallBack refreshRankingCallBack, User user, double currentDistance) {

        db.collection("users").document(user.getUid()).collection("rewards").whereEqualTo("finished", false)
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
                        db.collection("users").document(user.getUid()).collection("rewards")
                                .document(document.getId()).set(unfinishedReward);
                    }
                } else {
                    System.out.println("Error when getting rewards");
                }
            }
        });

        // todo concurrent, must wait after total distance is updated
        db.collection("users").document(user.getUid())
                .collection("totalDistance").document("total").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        double total = ((Number) documentSnapshot.get("distance")).doubleValue();

                        db.collection("leaderboard")
                                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            List<Leader> leaders = new ArrayList<>();
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    Map<String, Object> map = queryDocumentSnapshot.getData();
                                    String uid = (String) map.get("id");
                                    String name = (String) map.get("name");
                                    double distance = ((Number) map.get("distance")).doubleValue();
                                    int ranking = ((Number) map.get("ranking")).intValue();

                                    Leader leader = new Leader();
                                    leader.setId(uid);
                                    leader.setName(name);
                                    leader.setDistance(distance);
                                    leader.setRanking(ranking);
                                    leaders.add(leader);
                                }
                            }

                            leaders.sort((l1, l2) -> l1.getDistance() == l2.getDistance() ?
                                    l1.getRanking() - l2.getRanking() : Double.compare(l2.getDistance(), l1.getDistance()));

                            if (leaders.size() < 10 || (total > leaders.get(leaders.size() - 1).getDistance())) {
                                Optional<Leader> first = leaders.stream().filter(l -> l.getId().equals(user.getUid())).findFirst();
                                if (first.isPresent()) {
                                    Leader updatedLeader = first.get();
                                    updatedLeader.setDistance(total);
                                } else {
                                    if (leaders.size() == 10) {
                                        leaders.remove(leaders.size() - 1);
                                    }
                                    Leader newLeader = new Leader();
                                    newLeader.setId(user.getUid());
                                    newLeader.setName(user.getUsername());
                                    newLeader.setDistance(total);
                                    leaders.add(newLeader);
                                }
                                leaders.sort((l1, l2) -> l1.getDistance() == l2.getDistance() ?
                                        l1.getRanking() - l2.getRanking() : Double.compare(l2.getDistance(), l1.getDistance()));
                                for (int i = 0; i < leaders.size(); i++) {
                                    Leader leader = leaders.get(i);
                                    leader.setRanking(i);
                                    db.collection("leaderboard").document(String.valueOf(leader.getRanking())).set(leader);
                                }
                                refreshRankingCallBack.callBack(0);
                            }
                        });
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
                callBack.callBack(rewardList);
            }

        });
    }

    public void getLeaderboard(LeaderboardCallBack callBack) {
        db.collection("leaderboard")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                List<Leader> leaders = new ArrayList<>();
                queryDocumentSnapshots.forEach(queryDocumentSnapshot -> {
                    Map<String, Object> map = queryDocumentSnapshot.getData();
                    String id = (String) map.get("id");
                    String name = (String) map.get("name");
                    double distance = ((Number) map.get("distance")).doubleValue();
                    int ranking = ((Number) map.get("ranking")).intValue();

                    Leader leader = new Leader();
                    leader.setId(id);
                    leader.setName(name);
                    leader.setDistance(distance);
                    leader.setRanking(ranking);
                    leaders.add(leader);
                });
                leaders.sort((l1, l2) -> l1.getRanking() - l2.getRanking());
                callBack.callBack(leaders);
            }

        });
    }

    //use callback to retrieve data from firebase.
    public interface CallBack {
        void callBack(List<Reward> rewardList);
    }

    //use callback to retrieve data from firebase.
    public interface LeaderboardCallBack {
        void callBack(List<Leader> rewardList);
    }

    public interface TotalDistanceCallBack {
        void callBack(Double distance);
    }

    public interface RefreshRewardsCallBack {
        void callBack(int i);
    }

    public interface RefreshRankingCallBack {
        void callBack(int i);
    }

}
