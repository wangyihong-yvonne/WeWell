package edu.neu.madcourse.wewell.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.wewell.model.Activity;

public class UserService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "UserService";

    public void saveUser(String uid) {
        // Add a new user document with a custom ID
        Map<String, Object> userData = new HashMap<>();
        userData.put("activities", new ArrayList<Activity>());
        db.collection("users")
                .document(uid)
                .set(userData);
    }


}
