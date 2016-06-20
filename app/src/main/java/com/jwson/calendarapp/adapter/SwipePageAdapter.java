package com.jwson.calendarapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jwson.calendarapp.fragment.CalendarFragment;
import com.jwson.calendarapp.fragment.NotificationFragment;
import com.jwson.calendarapp.fragment.TimelineFragment;

/**
 * Created by jason_000 on 12/06/2016.
 */
public class SwipePageAdapter extends FragmentPagerAdapter {

    private static int NUM_PAGES = 3;

    public SwipePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CalendarFragment.newInstance();
            case 1:
                return TimelineFragment.newInstance();
            case 2:
                return NotificationFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
