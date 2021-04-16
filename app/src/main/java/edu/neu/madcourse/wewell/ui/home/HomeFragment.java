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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.Chart;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.SignInActivity;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.model.ActivitySummary;
import edu.neu.madcourse.wewell.service.ActivityService;
import edu.neu.madcourse.wewell.util.Util;

public class HomeFragment extends Fragment {

    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    private ActivityService activityService;

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    private RecyclerView recyclerViewHorizontal;
    private RecyclerView.LayoutManager rLayoutMangerHorizontal;
    private ComplexRecyclerViewAdapter complexRecyclerViewAdapter;


    private AnyChartView anyChartView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        activityService = new ActivityService();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);

        Button signOutButton = (Button) root.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        //new
        TextView textView = (TextView) root.findViewById(R.id.textView);
        String username = currentUserId;
        textView.setText(username);

        init(true, false, currentUserId);

        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
        return root;
    }


    private void init(boolean shouldCreateRecycler, boolean shouldNotifyDataChange, String currentUserId) {
        activityService.getActivitiesFromUser(new ActivityService.callBack() {
            @Override
            public void callBack(List<Activity> activityList) {
                if (activityList != null) {
                    int totalRun = activityList.size();
                    int totalCalorie = 0;
                    double totalDistance = 0;
                    long totalPace = 0;
                    for (Activity activity : activityList) {
                        totalCalorie += activity.getCalories();
                        totalDistance += activity.getDistance();
                        totalPace += activity.getPace();
                    }
                    int avgCalorie = totalCalorie / totalRun;
                    long avgPace = totalPace / totalRun;

                    String formattedAvgPace = Util.formatTime(avgPace);
                    String formattedAvgCalorie = String.valueOf(avgCalorie);
                    String formattedTotalRuns = String.valueOf(totalRun);
                    String formattedTotalDistance = String.format("%.2f", totalDistance);
                    ActivitySummary activitySummary = new ActivitySummary(formattedTotalDistance,
                            formattedTotalRuns, formattedAvgPace, formattedAvgCalorie);

                    List<Object> horizontalItemList = new ArrayList<>();
                    Cartesian cartesian = initColCharts(activityList);
                    horizontalItemList.add(activitySummary);
                    horizontalItemList.add(cartesian);
                    if (shouldCreateRecycler) {
                        createRecyclerVertical(activityList);
                        createRecyclerHorizontal(horizontalItemList);
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

    private void createRecyclerVertical(List<Activity> activityList) {
        rLayoutManger = new LinearLayoutManager(getContext());
        recyclerView = getView().findViewById(R.id.user_activity_list_recycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new RviewAdapter(activityList, getContext());
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    //TODO
    private void createRecyclerHorizontal(List<Object> itemList) {
        rLayoutMangerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal = getView().findViewById(R.id.user_activity_horizontal_recycler);
        recyclerViewHorizontal.setHasFixedSize(true);
        complexRecyclerViewAdapter = new ComplexRecyclerViewAdapter(itemList);
        recyclerViewHorizontal.setAdapter(complexRecyclerViewAdapter);
        recyclerViewHorizontal.setLayoutManager(rLayoutMangerHorizontal);
    }

    private Cartesian initColCharts(List<Activity> activityList) {
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (Activity activity : activityList) {
            String date = Util.formatDateV2(activity.getStartTime());
            double distance = activity.getDistance();
            data.add(new ValueDataEntry(date, distance));
        }
        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(2d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);
        cartesian.yScale().maximum(20d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yAxis(0).title("Distance (KM)");
        cartesian.xScroller(true);
//        cartesian.xScroller().thumbs().autoHide(true);
//        cartesian.xScroller().thumbs().hovered("#FFD700");

//        cartesian.xZoom().setToPointsCount(10,true, )
        anyChartView.setChart(cartesian);
        return cartesian;
    }


}