package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class ManageActivity extends AppCompatActivity {
    private static final String TAG = "ManageActivity";

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ManagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_switch);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setViewPager();
    }

    private void setViewPager() {
        Log.v(TAG, "Entering setViewPager");

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ManagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
