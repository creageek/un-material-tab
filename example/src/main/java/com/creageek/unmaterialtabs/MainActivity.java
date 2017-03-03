package com.creageek.unmaterialtabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ruslankishai.unmaterialtab.tabs.RoundTabLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new ViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        RoundTabLayout tabView = (RoundTabLayout) findViewById(R.id.round_tab_view);
        tabView.setupWithViewPager(viewPager);
        viewPager = (ViewPager) findViewById(R.id.view_pager2);
        viewPager.setAdapter(adapter);
        ((RoundTabLayout) findViewById(R.id.round_tab_view2)).setupWithViewPager(viewPager);
        viewPager = (ViewPager) findViewById(R.id.view_pager3);
        viewPager.setAdapter(adapter);
        ((RoundTabLayout) findViewById(R.id.round_tab_view3)).setupWithViewPager(viewPager);
        viewPager = (ViewPager) findViewById(R.id.view_pager4);
        viewPager.setAdapter(adapter);
        ((RoundTabLayout) findViewById(R.id.round_tab_view4)).setupWithViewPager(viewPager);
    }

    private class ViewAdapter extends FragmentPagerAdapter {

        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Articles";
                case 2:
                    return "Interviews";
                case 3:
                    return "News";
                case 4:
                    return "Events";
                case 5:
                    return "Links";
            }
            return "large tab text" + position;
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentTest().setFragmentText((String) getPageTitle(position));
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
