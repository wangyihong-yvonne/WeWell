package edu.neu.madcourse.wewell.ui.rewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import edu.neu.madcourse.wewell.R;

public class MainFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.reward_main, container, false);
        super.onCreate(savedInstanceState);

        tabLayout = root.findViewById(R.id.tab_layout);
        pager2 = root.findViewById(R.id.view_pager2);

        //FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentManager fm = getChildFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("LeaderBoard"));
        tabLayout.addTab(tabLayout.newTab().setText("Badges"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        return root;

    }

}
