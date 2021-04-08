package edu.neu.madcourse.wewell.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.LinkedList;
import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.SignInActivity;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.service.ActivityService;

public class HomeFragment extends Fragment {

    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    private List<Activity> activityList = new LinkedList<>();

    private ActivityService activityService;
    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        activityService = new ActivityService();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);

        Button signOutButton = (Button) root.findViewById(R.id.sign_out_button);
//        Button getUserActivitiesButton = (Button) root.findViewById(R.id.get_activities);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        //Button shareButton =

        init(true, false, currentUserId);
//        getUserActivitiesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return root;
    }


    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        activityService.getActivitiesFromUser(new ActivityService.callBack() {
            @Override
            public void callBack(List<Activity> activityList) {
                if (activityList != null) {
                    if (shouldCreateRecycler) {
                        createRecycler(activityList);
                    }
                    if (shouldNotifyDataChange) {
                        rviewAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, currentUserId);
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        // [END auth_fui_signout]
    }

    private void createRecycler(List<Activity> activityList) {
        rLayoutManger = new LinearLayoutManager(getContext());
        recyclerView = getView().findViewById(R.id.user_activity_list_recycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new RviewAdapter(activityList,getContext());
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

//    public void refreshUserList(View view) {
//        init(false, true, );
//    }


}