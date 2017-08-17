package com.hedyhidoury.calendar.horizontallibrary.views;

/**
 * Created by hidou on 7/31/2017.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.hedyhidoury.calendar.horizontallibrary.adapter.WeekPagerAdapter;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnWeekChangeListener;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Weeks;

import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.selectedDateTime;


public class WeekPager extends ViewPager {
    private WeekPagerAdapter adapter;
    private int pos;
    private boolean check;
    public static int NUM_OF_PAGES;
    private boolean isFromMonthSwipeGesture = false;
    private DateTime currentDate = new DateTime();
    private OnWeekChangeListener listener;
    private OnMonthChangeListener monthListener;

    public WeekPager(Context context, int numOfWeeks, int viewId, OnWeekChangeListener listener, OnMonthChangeListener monthListener) {
        super(context);
        this.listener = listener;
        this.monthListener = monthListener;
        setId(viewId);
        initialize(numOfWeeks);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() {
                // Force rerendering so the week is drawn again when you return to the view after
                // back button press.
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Initialize number of weeks
     * @param numOfWeeks
     */
    private void initialize(int numOfWeeks) {
        NUM_OF_PAGES = numOfWeeks;
        if (!isInEditMode()) {
            initPager(new DateTime());
            BusProvider.getInstance().register(this);
        }
    }

    /**
     * Initiate pager
     * @param dateTime
     */
    private void initPager(DateTime dateTime) {
        pos = NUM_OF_PAGES / 2;
        adapter = new WeekPagerAdapter(((AppCompatActivity) getContext())
                .getSupportFragmentManager(), dateTime, listener, monthListener);
        setAdapter(adapter);
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (!check)
                    if (position < pos)
                        adapter.swipePreviousToPosition(isFromMonthSwipeGesture,position);
                    else if (position > pos)
                        adapter.swipeForwardToPosition(isFromMonthSwipeGesture,position);
                currentDate = adapter.getCurrentDate();
                isFromMonthSwipeGesture = false;
                pos = position;
                check = false;
            }
        });
        setOverScrollMode(OVER_SCROLL_NEVER);
        setCurrentItem(pos);

        if (selectedDateTime == null)
            selectedDateTime = new DateTime();
    }

    @Subscribe
    public void setCurrentPage(Event.SetCurrentPageEvent event) {
        check = true;
        if (event.getDirection() == 1)
            adapter.swipeForward(isFromMonthSwipeGesture);
        else
            adapter.swipeBack(isFromMonthSwipeGesture);
        setCurrentItem(getCurrentItem() + event.getDirection());

    }

    @Subscribe
    public void onSwipeRequested(Event.OnSwipeWeekRequested event) {
        if (adapter != null) {
            if (event.isForward()) {
                setCurrentItem(getCurrentItem() + 1);
            } else {
                setCurrentItem(getCurrentItem() - 1);
            }
        }
    }


    @Subscribe
    public void reset(Event.ResetEvent event) {
        selectedDateTime = new DateTime(WeekFragment.CalendarStartDate);
        //WeekFragment.CalendarStartDate = new DateTime();
        initPager(WeekFragment.CalendarStartDate);
    }

    @Subscribe
    public void setSelectedDate(Event.SetSelectedDateEvent event) {
        selectedDateTime = event.getSelectedDate();
        initPager(event.getSelectedDate());
    }

    @Subscribe
    public void setSelectedDate(Event.OnWeekChange event) {

    }

    @Subscribe
    public void setStartDate(Event.SetStartDateEvent event) {
        WeekFragment.CalendarStartDate = event.getStartDate();
        selectedDateTime = event.getStartDate();
        initPager(event.getStartDate());
    }

    @Subscribe
    public void onSwipeRequested(Event.OnSwipeMonthRequested event) {
        if (adapter != null) {
            isFromMonthSwipeGesture = event.isFromMonthSwipe();
        }
    }

    public void onMonthDateChanged(DateTime dateTime, boolean isForward) {
        if(adapter != null){
            currentDate = adapter.getCurrentDate();
            if(dateTime.getMonthOfYear() != currentDate.getMonthOfYear()){
                isFromMonthSwipeGesture = true;
                // this weeks can take also negative numbers
                int weeks = Weeks.weeksBetween(currentDate, dateTime.withDayOfWeek(DateTimeConstants.THURSDAY)).getWeeks();
                if(isForward){
                    setCurrentItem(getCurrentItem() + weeks  + 1);
                }else{
                    setCurrentItem(getCurrentItem() + weeks);
                }

            }
        }
    }

}
