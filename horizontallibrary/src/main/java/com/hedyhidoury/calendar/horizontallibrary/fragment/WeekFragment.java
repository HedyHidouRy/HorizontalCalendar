package com.hedyhidoury.calendar.horizontallibrary.fragment;

/**
 * Created by hidou on 7/31/2017.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedyhidoury.calendar.horizontallibrary.R;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


import static com.hedyhidoury.calendar.horizontallibrary.HorizontalCalendarView.inValidatedDays;
import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.selectedDateTime;

public class WeekFragment extends Fragment {
    public static String DATE_KEY = "date_key";
    private GridView gridView;
    private ImageView previousWeekImage;
    private ImageView forwardWeekImage;
    private WeekAdapter weekAdapter;
    private static boolean isDateSelected = false;
    public static DateTime CalendarStartDate = new DateTime();   // calendar start date, actual date
    private DateTime startDate; // start date of the week
    private DateTime endDate; // end date of the week
    private boolean isVisible;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.week_view, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        previousWeekImage = (ImageView)  rootView.findViewById(R.id.left_week_icon);
        forwardWeekImage = (ImageView)  rootView.findViewById(R.id.right_week_icon);

        init();
        return rootView;
    }

    private void init() {

        // decorate previous and forward icon
        BusProvider.getInstance().post(new Event.OnWeekDecorateEvent(previousWeekImage, forwardWeekImage));
        // days of the week
        ArrayList<DateTime> days = new ArrayList<>();
        // get date time from args
        DateTime midDate = (DateTime) getArguments().getSerializable(DATE_KEY);
        if (midDate != null) {
            midDate = midDate.withDayOfWeek(DateTimeConstants.THURSDAY);
        }
        // Getting all seven days
        for (int i = -3; i <= 3; i++)
            days.add(midDate != null ? midDate.plusDays(i) : null);

        startDate = days.get(0);
        endDate = days.get(days.size() - 1);

        weekAdapter = new WeekAdapter(getActivity(), days);
        gridView.setAdapter(weekAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if item clicked is not in the invalidated dates, make necessary changes
                if(!inValidatedDays.contains(position + 1)){
                    BusProvider.getInstance().post(new Event.OnDateClickEvent(weekAdapter.getItem
                            (position)));
                    isDateSelected = true;
                    selectedDateTime = weekAdapter.getItem(position);
                    BusProvider.getInstance().post(new Event.InvalidateEvent());

                }

            }
        });

        previousWeekImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new Event.OnSwipeWeekRequested(false));
            }
        });
        forwardWeekImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new Event.OnSwipeWeekRequested(true));
            }
        });

    }

    @Subscribe
    public void updateSelectedDate(Event.UpdateSelectedDateEvent event) {
        if (isVisible) {
            selectedDateTime = selectedDateTime.plusDays(event.getDirection());
            if (selectedDateTime.toLocalDate().equals(endDate.plusDays(1).toLocalDate())
                    || selectedDateTime.toLocalDate().equals(startDate.plusDays(-1).toLocalDate())) {
                if (!(selectedDateTime.toLocalDate().equals(startDate.plusDays(-1).toLocalDate()) &&
                        event.getDirection() == 1)
                        && !(selectedDateTime.toLocalDate().equals(endDate.plusDays(1)
                        .toLocalDate()) && event.getDirection() == -1))
                    BusProvider.getInstance().post(new Event.SetCurrentPageEvent(event.getDirection()));
            }
            BusProvider.getInstance().post(new Event.InvalidateEvent());
        }
    }


    @Subscribe
    public void invalidate(Event.InvalidateEvent event) {
        gridView.invalidateViews();
    }

    @Subscribe
    public void updateUi(Event.OnUpdateUi event) {
        weekAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        BusProvider.getInstance().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Week adapter for drawing days of the week
     * Taking responsablity of every change
     */
    public class WeekAdapter extends BaseAdapter {
        private ArrayList<DateTime> days;
        private Context context;
        private DateTime firstDay;

        WeekAdapter(Context context, ArrayList<DateTime> days) {
            this.days = days;
            this.context = context;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public DateTime getItem(int position) {
            return days.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.week_day_item, null);
                firstDay = getItem(0);
            }

            DateTime dateTime = getItem(position).withMillisOfDay(0);

            TextView dayTextView = (TextView) convertView.findViewById(R.id.daytext);
            TextView dayNameView = (TextView) convertView.findViewById(R.id.day_name);
            dayTextView.setText(String.valueOf(dateTime.getDayOfMonth()));
            // set the day name with 2 chars with first one as uppercase
            DateTime.Property pDoW = dateTime.dayOfWeek();
            String dayName = pDoW.getAsText(Locale.getDefault()).substring(0,2);
            // // TODO: 8/17/2017 make day name view on decorator
            dayNameView.setText(dayName.substring(0, 1).toUpperCase()+dayName.substring(1));
            if(inValidatedDays.contains(position + 1)){
                BusProvider.getInstance().post(new Event.OnDayDecorateEvent(convertView, dayTextView,dayNameView,
                        dateTime, firstDay, selectedDateTime,true));
                dayNameView.setTextColor(getResources().getColor(R.color.invalidate_day));
            }else{
                BusProvider.getInstance().post(new Event.OnDayDecorateEvent(convertView, dayTextView,dayNameView,
                        dateTime, firstDay, selectedDateTime,false));
            }

            return convertView;
        }
    }




}
