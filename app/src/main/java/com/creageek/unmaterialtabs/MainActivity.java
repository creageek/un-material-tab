package com.creageek.unmaterialtabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ruslankishai.unmaterialtab.tabs.RoundTabView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new ViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        RoundTabView tabView = (RoundTabView) findViewById(R.id.round_tab_view);
        tabView.setupWithViewPager(viewPager);

    }

    private class ViewAdapter extends FragmentPagerAdapter {

        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
               /* case 0:return "Ruslan is awesome";
                case 1:return "Force LLC";*/
                case 0:
                    return "heartrate";
                case 1:
                    return "oxygen";
                case 2:
                    return "emotional";
                case 3:
                    return "battery";
            }
            return "large tab text" + position;
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentTest().setFragmentText((String) getPageTitle(position));
        }

        @Override
        public int getCount() {
            return 8;
        }
    }


}
