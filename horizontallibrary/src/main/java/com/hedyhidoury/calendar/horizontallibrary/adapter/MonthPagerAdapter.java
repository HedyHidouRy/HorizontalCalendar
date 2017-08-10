package com.hedyhidoury.calendar.horizontallibrary.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


import com.hedyhidoury.calendar.horizontallibrary.fragment.MonthFragment;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;

import org.joda.time.DateTime;

import static com.hedyhidoury.calendar.horizontallibrary.fragment.MonthFragment.DATE_KEY;
import static com.hedyhidoury.calendar.horizontallibrary.views.MonthPager.NUM_OF_MONTHS_PAGES;


/**
 * Created by hidou on 8/2/2017.
 */

public class MonthPagerAdapter extends FragmentStatePagerAdapter {

    private int currentPage = NUM_OF_MONTHS_PAGES/2;
    private OnMonthChangeListener listener;

    private DateTime date;
    public MonthPagerAdapter(FragmentManager fm, DateTime date, OnMonthChangeListener listener) {
        super(fm);
        this.date = date;
        this.listener = listener;
    }


    @Override
    public Fragment getItem(int position) {
        MonthFragment fragment = new MonthFragment();

        Bundle bundle = new Bundle();

        if (position < currentPage)
            bundle.putSerializable(DATE_KEY, getPerviousDate(currentPage - position));
        else if (position > currentPage)
            bundle.putSerializable(DATE_KEY, getNextDate(position - currentPage));
        else
            bundle.putSerializable(DATE_KEY, getTodaysDate());

        fragment.setArguments(bundle);
        return fragment;
    }

    private DateTime getTodaysDate() {

        return date;
    }

    private DateTime getPerviousDate(int difference) {
        return date.plusMonths(-difference);
    }

    private DateTime getNextDate(int difference) {
        return date.plusMonths(difference);
    }


    /**
     * Swiping back
     * @param isFromSwipeGesture
     */
    public void swipeBack(boolean isFromSwipeGesture) {
        date = date.plusMonths(-1);
        currentPage--;
        currentPage = currentPage <= 1 ? NUM_OF_MONTHS_PAGES / 2 : currentPage;
        if(isFromSwipeGesture){
            listener.onMonthChange(date,false);
        }
    }

    /**
     * Swipe forward in week view
     * @param isFromSwipeGesture
     */
    public void swipeForward(boolean isFromSwipeGesture) {
        date = date.plusMonths(1);
        currentPage++;
        currentPage = currentPage >= NUM_OF_MONTHS_PAGES - 1 ? NUM_OF_MONTHS_PAGES / 2 : currentPage;
        if(isFromSwipeGesture){
            listener.onMonthChange(date,true);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return NUM_OF_MONTHS_PAGES;
    }


    @Override
    public int getItemPosition(Object object) {
        //Force rerendering so the week is drawn again when you return to the view after
        // back button press.
        return POSITION_NONE;
    }


}