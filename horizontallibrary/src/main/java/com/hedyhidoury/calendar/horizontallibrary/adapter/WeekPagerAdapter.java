package com.hedyhidoury.calendar.horizontallibrary.adapter;

/**
 * Created by hidou on 7/31/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnWeekChangeListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import static com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment.DATE_KEY;
import static com.hedyhidoury.calendar.horizontallibrary.views.WeekPager.NUM_OF_PAGES;


/**
 * Created by nor on 12/4/2015.
 */
public class WeekPagerAdapter extends FragmentStatePagerAdapter {
    public static final int WEEK_DAYS_NUMBER = 7;
    private int currentPage = NUM_OF_PAGES / 2;
    private DateTime date;
    private OnWeekChangeListener listener;
    private OnMonthChangeListener monthListener;

    public WeekPagerAdapter(FragmentManager fm, DateTime date, OnWeekChangeListener listener, OnMonthChangeListener monthListener) {
        super(fm);
        this.listener = listener;
        this.monthListener = monthListener;
        this.date = date;
    }

    @Override
    public Fragment getItem(int position) {
        WeekFragment fragment = new WeekFragment();

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

    @Override
    public int getCount() {
        return NUM_OF_PAGES;
    }

    private DateTime getTodaysDate() {
        return date;
    }

    private DateTime getPerviousDate(int weeksNumberDifference) {
        return date.plusDays(-weeksNumberDifference * WEEK_DAYS_NUMBER);
    }

    private DateTime getNextDate(int weeksNumberDifference) {
        return date.plusDays(weeksNumberDifference * WEEK_DAYS_NUMBER);
    }

    @Override
    public int getItemPosition(Object object) {
        //Force rerendering so the week is drawn again when you return to the view after
        // back button press.
        return POSITION_NONE;
    }

    /**
     * swipe back in week view
     *
     * @param isFromMonthSwipeGesture
     */
    public void swipeBack(boolean isFromMonthSwipeGesture) {
        boolean isPreviousMonth = false;
        DateTime previousDate = date.plusDays(-WEEK_DAYS_NUMBER);
        if (date.getMonthOfYear() != previousDate.getMonthOfYear()) {
            isPreviousMonth = true;
        }
        date = previousDate;
        currentPage--;
        currentPage = currentPage <= 1 ? NUM_OF_PAGES / 2 : currentPage;
        BusProvider.getInstance().post(
                new Event.OnWeekChange(date.withDayOfWeek(DateTimeConstants.MONDAY), false));

        if (isPreviousMonth && !isFromMonthSwipeGesture) {
            // BusProvider.getInstance().post(new Event.OnSwipeMonthRequested(false,false));
            monthListener.onMonthChangeFromWeeks(date, false);
        }

    }

    /**
     * Swipe forward in week view
     *
     * @param isFromMonthSwipeGesture
     */
    public void swipeForward(boolean isFromMonthSwipeGesture) {

        boolean isNextMonth = false;
        DateTime nextDate = date.plusDays(WEEK_DAYS_NUMBER);
        if (date.getMonthOfYear() != nextDate.getMonthOfYear()) {
            isNextMonth = true;
        }
        date = nextDate;
        currentPage++;


        currentPage = currentPage >= NUM_OF_PAGES - 1 ? NUM_OF_PAGES / 2 : currentPage;
        BusProvider.getInstance().post(
                new Event.OnWeekChange(date.withDayOfWeek(DateTimeConstants.MONDAY), true));

       /* if (isNextMonth && !isFromMonthSwipeGesture){
            BusProvider.getInstance().post(new Event.OnSwipeMonthRequested(true,false));
        }*/
        listener.onWeekChange(date, true);

    }

    /**
     * swipe back in week view
     *
     * @param isFromMonthSwipeGesture
     */
    public void swipePreviousToPosition(boolean isFromMonthSwipeGesture, int position) {
        boolean isPreviousMonth = false;

        DateTime previousDate = date.plusDays((position - currentPage) * WEEK_DAYS_NUMBER);
        if (date.getMonthOfYear() != previousDate.getMonthOfYear()) {
            isPreviousMonth = true;
        }
        date = previousDate;
        currentPage = position;
        currentPage = currentPage <= 1 ? NUM_OF_PAGES / 2 : currentPage;
        BusProvider.getInstance().post(
                new Event.OnWeekChange(date.withDayOfWeek(DateTimeConstants.MONDAY), false));

        if (isPreviousMonth && !isFromMonthSwipeGesture) {
            // BusProvider.getInstance().post(new Event.OnSwipeMonthRequested(false,false));
            monthListener.onMonthChangeFromWeeks(date, false);
        }
    }

    /**
     * Swipe forward in week view
     *
     * @param isFromMonthSwipeGesture
     */
    public void swipeForwardToPosition(boolean isFromMonthSwipeGesture, int position) {

        boolean isNextMonth = false;
        DateTime nextDate = date.plusDays((position - currentPage) * WEEK_DAYS_NUMBER);
        if (date.getMonthOfYear() != nextDate.getMonthOfYear()) {
            isNextMonth = true;
        }
        date = nextDate;
        currentPage = position;

        currentPage = currentPage >= NUM_OF_PAGES - 1 ? NUM_OF_PAGES / 2 : currentPage;
        BusProvider.getInstance().post(
                new Event.OnWeekChange(date.withDayOfWeek(DateTimeConstants.MONDAY), true));

        if (isNextMonth && !isFromMonthSwipeGesture) {
            // BusProvider.getInstance().post(new Event.OnSwipeMonthRequested(true,false));
            monthListener.onMonthChangeFromWeeks(date, true);
        }
    }


    public DateTime getCurrentDate() {
        return date;
    }




   /* public DateTime getDate() {
        return date;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public void setDate(DateTime date) {
        this.date = date;
    }
*/

}