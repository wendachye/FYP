package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wenda.tarucnfc.R;

import java.util.ArrayList;
import java.util.List;


public class BusScheduleFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public BusScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_schedule, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.bus_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.bus_tabs);
        tabLayout.setupWithViewPager(viewPager);
       /* TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();*/

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getFragmentManager());
        adapter.addFragment(new BusRouteFragment("Wangsa Maju"), "Wangsa Maju");
        adapter.addFragment(new BusRouteFragment("Genting Klang"), "Genting Klang");
        adapter.addFragment(new BusRouteFragment("PV10/12/13/15/16"), "PV10/12/13/15/16");
        adapter.addFragment(new BusRouteFragment("Melati Utama"), "Melati Utama");
        adapter.addFragment(new BusRouteFragment("Sri Rampai"), "Sri Rampai");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentList.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
