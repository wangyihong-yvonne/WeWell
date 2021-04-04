package edu.neu.madcourse.wewell.service;


import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public List<Activity> getActivitiesFromUser(String userId) {

        return null;
    }

}
