package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SwitchFragment extends Fragment {
    private static final String TAG = "SwitchFragment";

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ManagerAdapter mAdapter;
    private TabLayout mTabLayout;

    public static SwitchFragment newInstance() {
        return new SwitchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Entering getCreateView");

        final View view = inflater.inflate(R.layout.fragment_switch, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mAdapter = new ManagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Not used
            }

            @Override
            public void onPageSelected(int position) {
                //Force the fragment to reload its data
                Fragment f = mAdapter.getItem(position);
                f.onResume();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //Not used
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }
}

