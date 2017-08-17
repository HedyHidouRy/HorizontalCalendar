package com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl;

/**
 * Created by hidou on 7/31/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;


import com.hedyhidoury.calendar.horizontallibrary.R;
import com.hedyhidoury.calendar.horizontallibrary.decorator.DayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment;

import org.joda.time.DateTime;


/**
 * Created by gokhan on 7/27/16.
 */
public class DefaultDayDecorator implements DayDecorator {

    private Context context;
    private final int selectedDateColor;
    private final int todayDateColor;
    private int todayDateTextColor;
    private int textColor;
    private float textSize;
    private Typeface weekTypeFace;

    public DefaultDayDecorator(Context context,
                               @ColorInt int selectedDateColor,
                               @ColorInt int todayDateColor,
                               @ColorInt int todayDateTextColor,
                               @ColorInt int textColor,
                               float textSize,
                               Typeface weekTypeFace) {
        this.context = context;
        this.selectedDateColor = selectedDateColor;
        this.todayDateColor = todayDateColor;
        this.todayDateTextColor = todayDateTextColor;
        this.textColor = textColor;
        this.textSize = textSize;
        this.weekTypeFace = weekTypeFace;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void decorate(View view, TextView dayTextView,
                         TextView dayNameTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {
        //DateTime dt = new DateTime();

        Drawable holoCircle = ContextCompat.getDrawable(context, R.drawable.holo_circle);
        Drawable solidCircle = ContextCompat.getDrawable(context, R.drawable.solid_circle);

        holoCircle.setColorFilter(selectedDateColor, PorterDuff.Mode.SRC_ATOP);
        solidCircle.setColorFilter(todayDateColor, PorterDuff.Mode.SRC_ATOP);
        // solidCircle.mutate().setAlpha(200);
        //holoCircle.mutate().setAlpha(200);


        if (firstDayOfTheWeek.getMonthOfYear() < dateTime.getMonthOfYear()
                || firstDayOfTheWeek.getYear() < dateTime.getYear())
            dayTextView.setTextColor(Color.GRAY);

        DateTime calendarStartDate = WeekFragment.CalendarStartDate;

        if (selectedDateTime != null) {
            if (selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {
                if (!selectedDateTime.toLocalDate().equals(calendarStartDate.toLocalDate()))
                    dayTextView.setBackground(holoCircle);
            } else {
                dayTextView.setBackground(null);
            }
        }
        // testing to see if selected date ot not
        if (dateTime.toLocalDate().equals(calendarStartDate.toLocalDate())) {
            dayTextView.setBackground(solidCircle);
            dayTextView.setTextColor(this.todayDateTextColor);
        } else {
            dayTextView.setTextColor(textColor);
        }
        float size = textSize;
        if (size == -1)
            size = dayTextView.getTextSize();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        dayTextView.setTypeface(weekTypeFace);
        dayNameTextView.setTypeface(weekTypeFace);
    }

    @Override
    public void decorateInvalidate(View view, TextView dayTextView, TextView dayNameTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {

        dayTextView.setTextColor(context.getResources().getColor(R.color.invalidate_day));

        float size = textSize;
        if (size == -1)
            size = dayTextView.getTextSize();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        dayTextView.setTypeface(weekTypeFace);
        dayNameTextView.setTypeface(weekTypeFace);
    }


}