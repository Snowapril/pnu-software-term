package com.software_term.gitpnu;
import com.software_term.gitpnu.fragments.HomeFragment;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class HomeActivity extends AppCompatActivity {

    Button m_home_btn;
    Button m_notify_btn;
    Button m_action_btn;
    Button m_calendar_btn;
    Button m_profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_home_btn = (Button)findViewById(R.id.nav_home_btn);
        m_notify_btn = (Button)findViewById(R.id.nav_notify_btn);
        m_action_btn = (Button)findViewById(R.id.nav_action_btn);
        m_calendar_btn = (Button)findViewById(R.id.nav_calendar_btn);
        m_profile_btn = (Button)findViewById(R.id.nav_profile_btn);

        ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    };

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return HomeFragment.newInstance("FirstFragment", "Instance 1");
                default: return HomeFragment.newInstance("ThirdFragment", "Default");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
