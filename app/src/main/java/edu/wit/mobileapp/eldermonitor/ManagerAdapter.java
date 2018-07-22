package edu.wit.mobileapp.eldermonitor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ManagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "ManagerAdapter";

    private static int TAB_COUNT = 3;

    public ManagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "Entering getItem");

        switch (position) {
            case 0:
                return SearchFragment.newInstance();
            case 1:
                return ManageFragment.newInstance();
            case 2:
                return RequestFragment.newInstance();
        }
        return null;
    }



    @Override
    public int getCount() {
        Log.v(TAG, "Entering getCount");

        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.v(TAG, "Entering getPageTitle");

        switch (position) {
            case 0:
                return "Search";

            case 1:
                return "Manage";

            case 2:
                return "Requests";

        }

        return super.getPageTitle(position);
    }



}
