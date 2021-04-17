package edu.neu.madcourse.wewell.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.wewell.R;
import edu.neu.madcourse.wewell.model.Activity;
import edu.neu.madcourse.wewell.model.User;
import edu.neu.madcourse.wewell.service.ActivityService;
import edu.neu.madcourse.wewell.service.RewardService;
import edu.neu.madcourse.wewell.util.Util;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = DashboardFragment.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    // The entry point to the Places API.
    private PlacesClient placesClient;
    private ActivityService activityService;
    private RewardService rewardService;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private User currentUser;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private Location previousLocation;

    private Button btPause = null;
    private Button btRun = null;
    private Button btStop = null;
    private TextView textTime = null;
    private TextView textDistance = null;
    private TextView textPace = null;
    private TextView textCalories = null;
    private TextView textViewcongrats = null;
    private LinearLayout sublayout = null;
    private LinearLayout mainlayout = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Handler mHandler = null;

    private static int timeCount = 0; //in seconds
    private static long pace = 0; // milliseconds/km
    private static int calories = 0;
    private boolean isPause = false;
    private boolean isStop = true;
    private boolean isDrawRoute = false;
    private static int delay = 1000; //1s
    private static int period = 1000; //1s
    private static double totalDistance = 0;
    private static final int UPDATE_TEXTVIEW = 0;
    private static long startTime = 0;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // business related
    private List<Location> locationHistory = new ArrayList<>();
    private List<Polyline> routeLines = new ArrayList<>();


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        super.onCreate(savedInstanceState);

        // Construct a PlacesClient
        Places.initialize(getActivity(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btRun = (Button) root.findViewById(R.id.button_run);
        btPause = (Button) root.findViewById(R.id.button_pause);
        btStop = (Button) root.findViewById(R.id.button_stop);
        textTime = (TextView) root.findViewById(R.id.data_time);
        textDistance = (TextView) root.findViewById(R.id.data_distance);
        textPace = (TextView) root.findViewById(R.id.data_pace);
        textCalories = (TextView) root.findViewById(R.id.data_calories);
        textViewcongrats = (TextView) root.findViewById(R.id.congrats);
        sublayout=(LinearLayout)root.findViewById(R.id.sub);
        mainlayout = (LinearLayout)root.findViewById(R.id.main);



        btRun.setOnClickListener(listener);
        btPause.setOnClickListener(listener);
        btStop.setOnClickListener(listener);

        //initial buttons state
        btRun.setEnabled(true);
        btPause.setEnabled(false);
        btStop.setEnabled(false);

        activityService = new ActivityService();
        rewardService = new RewardService();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:
                        updateTextView();
                        break;
                    default:
                        break;
                }
            }
        };

        // get current user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUserEmail = sharedPreferences.getString(getString(R.string.current_user_email), null);
        String currentUsername = sharedPreferences.getString(getString(R.string.current_user_name), null);
        String currentUserId = sharedPreferences.getString(getString(R.string.current_user_id), null);
        currentUser = new User(currentUserEmail, currentUsername, currentUserId);
        System.out.println(currentUserEmail);
        System.out.println(currentUsername);
        System.out.println(currentUserId);

        return root;
    }

    /*chronometer*/
    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v == btRun) {
                routeLines.clear();
                map.clear();
                startTimer();
                btRun.setEnabled(false);
                btPause.setEnabled(true);
                btStop.setEnabled(true);
                isDrawRoute = true;
                textDistance.setText("0.00");
                textPace.setText("00'00''");
                textCalories.setText("0");
                startTime = System.currentTimeMillis();
            }

            if (v == btPause) {
                pauseTimer();
                btRun.setEnabled(false);
                btPause.setEnabled(true);
                btStop.setEnabled(true);
                if (isPause) {//pause
                    btPause.setText("Resume");
                    //stop drawing
                    isDrawRoute = false;
                } else { //resume
                    btPause.setText("Pause");
                    isDrawRoute = true;
                }
            }

            if (v == btStop) {
                if (isPause) {
                    isPause = !isPause;
                }

                btRun.setEnabled(true);
                btPause.setEnabled(false);
                btStop.setEnabled(false);
                textViewcongrats.setVisibility(View.VISIBLE);
                textViewcongrats.setText("Congratulations!You've made it. Here's a summary of your runs.");
                btRun.setVisibility(View.GONE);
                btPause.setVisibility(View.GONE);
                btStop.setVisibility(View.GONE);
//                sublayout.setVisibility(View.GONE);
//                mainlayout.setVisibility(View.VISIBLE);
                previousLocation = null;
                lastKnownLocation = null;
                Activity activity = new Activity(startTime, pace, totalDistance, timeCount, calories);
                activityService.saveActivity(activity, currentUser.getUid());
                // refresh activity history
                refreshFragment(getString(R.string.title_home));
                // update current user's total distance
                activityService.updateTotalDistance(curTotalDistance -> {
                    // update user's reward
                    rewardService.updateReward(cb2 -> {
                        // refresh rewards
                        refreshFragment(getString(R.string.title_rewards));
                    }, currentUser, curTotalDistance);
                }, currentUser.getUid(), totalDistance);

                stopTimer();
                isDrawRoute = false;
                totalDistance = 0;

            }
        }
    };


    private void refreshFragment(String fragmentName) {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(fragmentName);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    sendMessage(UPDATE_TEXTVIEW);
                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);
                    timeCount++; /*update time*/
                }
            };
        }

        if (mTimer != null) {
            mTimer.schedule(mTimerTask, delay, period);
        }

    }

    public void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

    private void pauseTimer() {
        isPause = !isPause;
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;

        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;

        }
        timeCount = 0;
    }

    private String getTimerText() {
        int rounded = Math.round(timeCount);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    //get formatted pace
    private String getPace() {
        long timeElapsed = System.currentTimeMillis() - startTime;
        System.out.println(timeElapsed);
        if (totalDistance > 0.001) {
            long p = (long) (timeElapsed / totalDistance / 1000);
            pace = p;
        }
        return Util.formatTime(pace);
    }

    // get formatted calories
    public String getCalories() {
        int c = (int) ((totalDistance * 60));
        calories = c;
        return String.valueOf(c);
    }

    private void updateTextView() {
        textTime.setText(getTimerText());
        double d = totalDistance;
        String formatDistance = String.format("%.2f", d);
        textDistance.setText(formatDistance);
        textPace.setText(getPace());
        textCalories.setText(getCalories());
    }

    @SuppressLint("MissingPermission")
    public void startTracking() {
        LocationRequest locationRequest = LocationRequest.create();
        // location update interval: 0.3 second
        locationRequest.setInterval(300);
        // fast location update interval: 0.01 second
        locationRequest.setFastestInterval(100);
        // request High accuracy location based on the need of this app
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationUpdate(locationResult.getLastLocation());
            }
        }, null);
    }

    public void onLocationUpdate(Location location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
        //draw the route only when the user is running
        if (location != null && isDrawRoute) {
            showRoute(location);
        }
    }

    private void showRoute(Location location) {
        if (locationHistory.isEmpty()) {
            previousLocation = location;
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(currentLocation).title("My start location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        } else {
            lastKnownLocation = location;
            previousLocation = locationHistory.get(locationHistory.size() - 1);
            double distance = lastKnownLocation.distanceTo(previousLocation);

            //add to the total distance 
            // only when the distance between current and previous locations is greater than 0.5 meter
            if (distance > 0.5) {
                totalDistance += lastKnownLocation.distanceTo(previousLocation) / 1000.0;
            }
            System.out.println("total distance: " + totalDistance);
        }
        locationHistory.add(location);

        PolylineOptions lineOptions = new PolylineOptions();
        // todo location update interval 0.03 same as drawing interval
        float distance = location.distanceTo(previousLocation);
        if (distance > 0.03) {
            lineOptions.add(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()))
                    .add(new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(10);
        }

        Polyline currentLine = map.addPolyline(lineOptions);
        currentLine.setZIndex(1000);
        routeLines.add(currentLine);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        getDeviceLocation();

        startTracking();
    }

    // Gets the current location of the device, and positions the map's camera.
    private void getDeviceLocation() {
        // Get the best and most recent location of the device, which may be null in rare
        // cases when a location is not available.
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}