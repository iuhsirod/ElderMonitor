package edu.wit.mobileapp.eldermonitor;
/**
 * Created by huid on 6/27/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ManagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 2;

    public ManagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return SearchFragment.newInstance();
            case 1:
                return ManageFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Search";

            case 1:
                return "Manage";

        }

        return super.getPageTitle(position);
    }
}
