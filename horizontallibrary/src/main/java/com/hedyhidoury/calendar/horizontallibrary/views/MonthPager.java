package com.hedyhidoury.calendar.horizontallibrary.views;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hedyhidoury.calendar.horizontallibrary.adapter.MonthPagerAdapter;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.selectedDateTime;

/**
 * Created by hidou on 8/2/2017.
 */

public class MonthPager extends ViewPager {
    private MonthPagerAdapter adapter;
    private int pos;
    private boolean check;
    public static int NUM_OF_MONTHS_PAGES;
    boolean isFromSwipeGesture = true;
    private OnMonthChangeListener listener;

    public MonthPager(Context context, int nbMonths, int viewId, OnMonthChangeListener listener) {
        super(context);
        this.listener = listener;
        setId(viewId);
        initialize(nbMonths);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() {
                //Force rerendering so the week is drawn again when you return to the view after
                // back button press.
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initialize(int monthsNumber) {
        NUM_OF_MONTHS_PAGES = monthsNumber;

        if (!isInEditMode()) {
            initPager(new DateTime());
            BusProvider.getInstance().register(this);
        }
    }

    private void initPager(DateTime dateTime) {
        pos = NUM_OF_MONTHS_PAGES / 2;
        adapter = new MonthPagerAdapter(((AppCompatActivity) getContext())
                .getSupportFragmentManager(), dateTime,this.listener);
        setAdapter(adapter);
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()  {
            @Override
            public void onPageSelected(int position) {
                if (!check)
                    if (position < pos)
                        adapter.swipeBack(isFromSwipeGesture);
                    else if (position > pos)
                        adapter.swipeForward(isFromSwipeGesture);
                isFromSwipeGesture = true;
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
    public void onSwipeRequestedFromArrows(Event.OnMonthChange event) {
        if (adapter != null) {
            onSwipeRequested(event.getMonthDay(),event.isForward(),true);
        }
    }

    public void onSwipeRequested(DateTime dateTime, boolean isForward, boolean isFromSwipeGesture) {
        if (adapter != null) {
            this.isFromSwipeGesture = isFromSwipeGesture;
            if (isForward) {
                setCurrentItem(getCurrentItem() + 1);
            } else {
                setCurrentItem(getCurrentItem() - 1);
            }
        }
    }
}
