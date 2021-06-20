package com.software_term.gitpnu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.software_term.gitpnu.fragments.ActionFragment;
import com.software_term.gitpnu.fragments.CalendarFragment;
import com.software_term.gitpnu.fragments.HomeFragment;
import com.software_term.gitpnu.fragments.NotifyFragment;
import com.software_term.gitpnu.fragments.ProfileFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HomeActivity extends AppCompatActivity {

    private String m_user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_user_token = getIntent().getStringExtra("token");

        ViewPager2 pager = (ViewPager2)findViewById(R.id.view_pager);
        pager.setPageTransformer(new ZoomOutPageTransformer());
        pager.setAdapter(new MyPagerAdapter(this));

        BottomNavigationView navView = (BottomNavigationView)findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.nav_notify:
                        pager.setCurrentItem(1);
                        return true;
                    case R.id.nav_action:
                        pager.setCurrentItem(2);
                        return true;
                    case R.id.nav_calendar:
                        pager.setCurrentItem(3);
                        return true;
                    case R.id.nav_profile:
                        pager.setCurrentItem(4);
                        return true;
                    default:
                        Log.e("BottomNavigationView", String.format("Unknown item ID %d", item.getItemId()));
                        return false;
                }
            }
        });
    };

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int pos) {
            Log.i("createFragment", String.format("Fragment pos : %d", pos));
            switch(pos) {
                case 0: return HomeFragment.newInstance(m_user_token);
                case 1: return NotifyFragment.newInstance(m_user_token);
                case 2: return ActionFragment.newInstance(m_user_token);
                case 3: return CalendarFragment.newInstance(m_user_token);
                case 4: return ProfileFragment.newInstance(m_user_token);
                default: return HomeFragment.newInstance(m_user_token);
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}
