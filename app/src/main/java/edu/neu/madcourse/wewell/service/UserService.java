package edu.neu.madcourse.wewell.service;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.model.Reward;

public class UserService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "UserService";

    public void saveUser(String uid) {
        // Add a new user document with a custom ID
        Map<String, Object> userData = new HashMap<>();
        userData.put("activities", new ArrayList<Activity>());
        db.collection("users")
                .document(uid)
                .set(userData).addOnSuccessListener(aVoid -> {
            // save default rewards
            CollectionReference defaultGoals = db.collection("DefaultGoals");
            defaultGoals.get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                if (queryDocumentSnapshots != null) {
                    documentSnapshots.forEach(documentSnapshot -> {
                        Map<String, Object> defaultReward = documentSnapshot.getData();
                        Reward reward = new Reward();
                        reward.setType(((Number) defaultReward.get("type")).intValue());
                        if (reward.getType() == 0) {
                            reward.setGoal(((Number) defaultReward.get("distance")).doubleValue());
                        } else {
                            reward.setGoal(((Number) defaultReward.get("calories")).doubleValue());
                        }
                        reward.setFinishedAmount(0);
                        reward.setTitle((String) defaultReward.get("title"));
                        reward.setFinished(false);

                        db.collection("users").document(uid).collection("rewards")
                                .document(documentSnapshot.getId()).set(reward);

                        Map<String, Double> map = new HashMap<>();
                        map.put("distance", 0.0);
                        // set total distance
                        db.collection("users").document(uid)
                                .collection("totalDistance")
                                .document("total").set(map);
                    });
                }
            });
        });

    }


}
