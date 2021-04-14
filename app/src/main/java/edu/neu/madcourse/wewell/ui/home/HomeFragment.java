package edu.neu.madcourse.wewell.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import edu.neu.madcourse.wewell.service.RewardService;

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
    private RewardService rewardService;
    private TextView textViewMiles;
    private TextView textViewRuns;
    private TextView avgDis;
    private TextView avgPace;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        activityService = new ActivityService();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);

        TextView textView = (TextView) root.findViewById(R.id.textView);
        String username = currentUserId;
        textView.setText(username);

        Button signOutButton = (Button) root.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        rewardService = new RewardService();
        init(true, false, currentUserId);
        textViewMiles = (TextView) root.findViewById(R.id.textView2);
        textViewRuns = (TextView) root.findViewById(R.id.textView3);
        avgDis = (TextView) root.findViewById(R.id.textView5);
        avgPace = (TextView) root.findViewById(R.id.textView11);
        // To do: setText avgDis & avgPace
        return root;
    }

    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        activityService.getActivitiesFromUser(activityList -> {
            if (activityList != null) {
                int totalrun = activityList.size();
                String total = String.valueOf(totalrun);
                textViewRuns.setText(total);
                if (shouldCreateRecycler) {
                    createRecycler(activityList);
                }
                if (shouldNotifyDataChange) {
                    rviewAdapter.notifyDataSetChanged();
                }
            }
        }, currentUserId);
       rewardService.getTotalDistanceByUser(distance -> {
            String formatDistance = String.format("%.2f", distance);
           textViewMiles.setText(formatDistance);
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