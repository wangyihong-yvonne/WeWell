package edu.neu.madcourse.wewell;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.neu.madcourse.wewell.ui.dashboard.DashboardFragment;
import edu.neu.madcourse.wewell.ui.home.HomeFragment;
import edu.neu.madcourse.wewell.ui.notifications.NotificationsFragment;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    final Fragment homeFragment = new HomeFragment();
    final Fragment dashboardFragment = new DashboardFragment();
    final Fragment notificationsFragment = new NotificationsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_dashboard);

        fm.beginTransaction().add(R.id.main_container, homeFragment, "home").hide(homeFragment).commit();
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "dashboard").commit();
        fm.beginTransaction().add(R.id.main_container, notificationsFragment, "notifications").hide(notificationsFragment).commit();
    }

    // see https://medium.com/@oluwabukunmi.aluko/bottom-navigation-view-with-fragments-a074bfd08711
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                return true;

            case R.id.navigation_dashboard:
                fm.beginTransaction().hide(active).show(dashboardFragment).commit();
                active = dashboardFragment;
                return true;

            case R.id.navigation_notifications:
                fm.beginTransaction().hide(active).show(notificationsFragment).commit();
                active = notificationsFragment;
                return true;
        }
        return false;
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                }
            }
        }
    }

}