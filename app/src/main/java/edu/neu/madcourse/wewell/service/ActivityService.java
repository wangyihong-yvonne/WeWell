package edu.neu.madcourse.wewell.service;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.wewell.model.Activity;

public class ActivityService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void saveActivity(Activity activity, String userId) {
        // Add a new user document with a custom ID
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("startTime", activity.getStartTime());
        activityMap.put("pace", activity.getPace());
        activityMap.put("distance", activity.getDistance());
        activityMap.put("runningTime", activity.getRunningTime());
        activityMap.put("calories", activity.getCalories());
        db.collection("users")
                .document(userId)
                .update("activities", FieldValue.arrayUnion(activityMap));
        //TODO call success or failure callback
    }

    //TODO this method can be used to get user's activity history
//    public List<Activity> getActivitiesFromUser(String userId, callBack callBack) {
//
//        return null;
//    }

    public void updateTotalDistance(RefreshTotalCallback callback, String userId, double currentDistance) {
        db.collection("users").document(userId)
                .collection("totalDistance").document("total").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        double prevTotalDistance = ((Number) documentSnapshot.get("distance")).doubleValue();
                        double newTotal = prevTotalDistance + currentDistance;
                        db.collection("users").document(userId)
                                .collection("totalDistance")
                                .document("total").update("distance", newTotal)
                        .addOnSuccessListener(aVoid -> {
                            callback.callBack(currentDistance);
                        });
                    }
                });
    }

    public void getActivitiesFromUser(callBack callBack, String userId) {
        DocumentReference documentReference = db.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<Map<String, Object>> activities = (List<Map<String, Object>>) documentSnapshot.get("activities"); // is this efficient?
                    List<Activity> activitiesList = new ArrayList<>();

                    for (Map<String, Object> map : activities) {
                        long startTime = (long) map.get("startTime");
                        long pace = (long) map.get("pace");
                        double distance = (double) map.get("distance");
                        long runningTime = (long) map.get("runningTime");
                        long calories = (long) map.get("calories");
                        DecimalFormat df = new DecimalFormat("#.##");
                        distance = Double.valueOf(df.format(distance));
                        Activity activity = new Activity(startTime, pace, distance, runningTime, (int) calories);
                        activitiesList.add(activity);
                    }

                    callBack.callBack(activitiesList);
                }
            }
        });
    }

    //use callback to retrieve data from firebase.
    public interface callBack {
        void callBack(List<Activity> activityList);

    }

    public interface RefreshTotalCallback {
        void callBack(double curTotalDistance);

    }

}


